package serialization.valueserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Hashtable;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import serialization.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.COLLECTION_SERIALIZER})
public class HashtableSerializer implements ValueSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject instanceof Hashtable) {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			Hashtable list = (Hashtable)anObject;
			// put length in first
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(Hashtable.class.toString().getBytes());
				((ByteBuffer)anOutputBuffer).putInt(list.size());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(Hashtable.class.toString() + list.size() + SerializerRegistry.DEMIN);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			// then list objects
			for (Object key : list.keySet()) {
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, key, visitedObjects);
				Object value = list.get(key);
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, value, visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} else {
			throw new NotSerializableException(anObject.toString() + " is not a Hashtable!");
		}	
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if (aClass == Hashtable.class) {
			ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
			Hashtable list = new Hashtable();
			retrievedObjects.add(list);
			Integer len = (Integer) SerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(anInputBuffer, Integer.class, retrievedObjects);				
			for (int i = 0; i < len; i++) {
				Object key = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				Object value = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				list.put(key, value);
			}
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, list, retrievedObjects);
			return list;
		} else {
			throw new NotSerializableException("given class is not Hashtable!");
		}
	}

}
