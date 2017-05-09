package serialization.valueserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import serialization.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

public class StringSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof String) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			String value = (String) anObject;
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(String.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putInt(value.length());
				((ByteBuffer)anOutputBuffer).put(value.getBytes());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(String.class.toString() + value.length() + SerializerRegistry.DEMIN + value);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not an String!");
		}	
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if (aClass == String.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			String value = null;			
			if (anInputBuffer instanceof ByteBuffer) {
				Integer len = ((ByteBuffer)anInputBuffer).getInt();
				byte[] bytes = new byte[len];
				((ByteBuffer)anInputBuffer).get(bytes, 0, len);
				value = new String(bytes);
			} else if (anInputBuffer instanceof StringBuffer) {
				int end = ((StringBuffer)anInputBuffer).indexOf(SerializerRegistry.DEMIN);
				int len = Integer.parseInt(((StringBuffer)anInputBuffer).toString().substring(0, end));
				((StringBuffer)anInputBuffer).delete(0, end+1);
				value = ((StringBuffer)anInputBuffer).toString().substring(0, len);
				((StringBuffer)anInputBuffer).delete(0, len);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} else {
			throw new NotSerializableException("given class is not String!");
		}
	}

}
