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

@Tags({Comp533Tags.NULL_SERIALIZER})
public class NullSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject == null) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			String value = "null";
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.NULL_HEADER.getBytes());
				((ByteBuffer)anOutputBuffer).put(value.getBytes());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(SerializerRegistry.NULL_HEADER + value);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException("Cannot serialize a non-null object");
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		String value = null;			
		if (anInputBuffer instanceof ByteBuffer) {
			byte[] bytes = new byte[4];
			((ByteBuffer)anInputBuffer).get(bytes, 0, 4);
			value = new String(bytes);
		} else if (anInputBuffer instanceof StringBuffer) {
			value = ((StringBuffer)anInputBuffer).toString().substring(0, 4);
			((StringBuffer)anInputBuffer).delete(0, 4);
		} else {
			throw new NotSerializableException("Buffer given unsupported!");
		}
		if (value.equals("null")) {
			return null;
		} else {
			throw new NotSerializableException("expected content to be \'null\', found otherwise");
		}
		
	}

}
