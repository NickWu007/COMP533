package serialization.typefreeserializer;

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

@Tags({Comp533Tags.ENUM_SERIALIZER})
public class EnumSerializer implements TypeFreeSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject.getClass().isEnum()) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			Enum value = (Enum) anObject;
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.CLASS_HEADER.getBytes());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(SerializerRegistry.CLASS_HEADER);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			visitedObjects.add(anObject);
			SerializerRegistry.getValueSerializer(String.class).objectToBuffer(anOutputBuffer, value.getClass().getName(), visitedObjects);
			SerializerRegistry.getValueSerializer(String.class).objectToBuffer(anOutputBuffer, value.toString(), visitedObjects);
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not an Enum!");
		}	
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
		String enumString = (String) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
		Enum value = Enum.valueOf(aClass, enumString);
		retrievedObjects.add(value);
		ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
		return value;
	}

}
