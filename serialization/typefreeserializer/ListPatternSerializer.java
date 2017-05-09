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
import util.misc.RemoteReflectionUtility;

@Tags({Comp533Tags.LIST_PATTERN_SERIALIZER})
public class ListPatternSerializer implements TypeFreeSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (RemoteReflectionUtility.isList(anObject.getClass())) {
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
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, RemoteReflectionUtility.listSize(anObject), visitedObjects);
			for (int i = 0; i < RemoteReflectionUtility.listSize(anObject); i++) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, RemoteReflectionUtility.listGet(anObject, i), visitedObjects);
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
		Object value;
		try {
			value = aClass.newInstance();
			retrievedObjects.add(value);
			Integer length = (Integer) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
			for (int i = 0; i < length; i++) {
				RemoteReflectionUtility.listAdd(value, SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
}
