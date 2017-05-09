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

@Tags({Comp533Tags.LONG_SERIALIZER})
public class LongSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof Long) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			Long value = (Long) anObject;
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(Long.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putLong(value);
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(Long.class.toString() + value.toString() + SerializerRegistry.DEMIN);
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
		if (aClass == Long.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Long value = null;			
			if (anInputBuffer instanceof ByteBuffer) {
				value = ((ByteBuffer)anInputBuffer).getLong();
			} else if (anInputBuffer instanceof StringBuffer) {
				int end = ((StringBuffer)anInputBuffer).indexOf(SerializerRegistry.DEMIN);
				value = Long.parseLong(((StringBuffer)anInputBuffer).toString().substring(0, end));
				((StringBuffer)anInputBuffer).delete(0, end+1);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} else {
			throw new NotSerializableException("given class is not Long!");
		}
	}

}
