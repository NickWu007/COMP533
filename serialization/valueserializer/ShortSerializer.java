package serialization.valueserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import serialization.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.SHORT_SERIALIZER})
public class ShortSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof Short) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			Short value = (Short) anObject;
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(Short.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putShort(value);
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(Short.class.toString() + value.toString() + SerializerRegistry.DEMIN);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not an integer!");
		}	
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if (aClass == Short.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Short value = null;			
			if (anInputBuffer instanceof ByteBuffer) {
				value = ((ByteBuffer)anInputBuffer).getShort();
			} else if (anInputBuffer instanceof StringBuffer) {
				int end = ((StringBuffer)anInputBuffer).indexOf(SerializerRegistry.DEMIN);
				value = Short.parseShort(((StringBuffer)anInputBuffer).toString().substring(0, end));
				((StringBuffer)anInputBuffer).delete(0, end+1);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} else {
			throw new NotSerializableException("given class is not Short!");
		}
	}

}
