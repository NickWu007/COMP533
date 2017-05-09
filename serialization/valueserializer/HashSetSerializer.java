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

@Tags({Comp533Tags.COLLECTION_SERIALIZER})
public class HashSetSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof HashSet) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			HashSet list = (HashSet)anObject;
			// put length in first
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(HashSet.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putInt(list.size());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(HashSet.class.toString() + list.size() + SerializerRegistry.DEMIN);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			// then list objects
			for (Object obj : list) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, obj, visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not a HashSet!");
		}	
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if (aClass == HashSet.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			HashSet list = new HashSet();
			retrievedObjects.add(list);
			Integer len = (Integer) SerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(anInputBuffer, Integer.class, retrievedObjects);				
			for (int i = 0; i < len; i++) {
				list.add(SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects));
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, list, retrievedObjects);
			return list;
		} else {
			throw new NotSerializableException("given class is not HashSet!");
		}
	}

}
