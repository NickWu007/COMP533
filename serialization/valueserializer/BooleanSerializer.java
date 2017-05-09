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

@Tags({Comp533Tags.BOOLEAN_SERIALIZER})
public class BooleanSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof Boolean) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			Integer value = (Boolean)anObject ? 0 : 1;
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(Boolean.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putInt(value);
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(Boolean.class.toString() + value.toString() + SerializerRegistry.DEMIN);
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
		if (aClass == Boolean.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Boolean value = null;			
			if (anInputBuffer instanceof ByteBuffer) {
				int raw = ((ByteBuffer)anInputBuffer).getInt();
				value = raw == 0;
			} else if (anInputBuffer instanceof StringBuffer) {
				int end = ((StringBuffer)anInputBuffer).indexOf(SerializerRegistry.DEMIN);
				Integer int_value = Integer.parseInt(((StringBuffer)anInputBuffer).toString().substring(0, end));
				value = int_value == 0;
				((StringBuffer)anInputBuffer).delete(0, end+1);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} else {
			throw new NotSerializableException("given class is not Boolean!");
		}
	}

}
