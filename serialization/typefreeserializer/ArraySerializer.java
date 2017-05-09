package serialization.typefreeserializer;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import serialization.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.ARRAY_SERIALIZER})
public class ArraySerializer implements TypeFreeSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject.getClass().isArray()) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.CLASS_HEADER.getBytes());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(SerializerRegistry.CLASS_HEADER);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			visitedObjects.add(anObject);
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, anObject.getClass().getName(), visitedObjects);
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, anObject.getClass().getComponentType().getName(), visitedObjects);
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, (Integer) Array.getLength(anObject), visitedObjects);
			for (int i = 0; i < Array.getLength(anObject); i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, Array.get(anObject, i), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not an Array!");
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
		String componentType = (String) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
		Class componentClass = null;
		try {
			componentClass = Class.forName(componentType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new NotSerializableException("ClassNotFound: " + componentType);
		}
		Integer length = (Integer) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
		Object value = Array.newInstance(componentClass, length);
		retrievedObjects.add(value);
		for (int i = 0; i < length; i++) {
			Array.set(value, i, SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
		}
		ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
		return value;
	}

}
