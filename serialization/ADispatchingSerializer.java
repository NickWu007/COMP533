package serialization;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({Comp533Tags.DISPATCHING_SERIALIZER})
public class ADispatchingSerializer implements DispatchingSerializer{
	

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (anObject == null) {
			SerializerRegistry.getNullSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
			return;
		}
		
		if (visitedObjects.contains(anObject)) {
			int offset = visitedObjects.indexOf(anObject);
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.REFERENCE_HEADER.getBytes());
				((ByteBuffer)anOutputBuffer).putInt(offset);
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(SerializerRegistry.REFERENCE_HEADER + offset + SerializerRegistry.DEMIN);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			return;
		}
		for (Class externalClass : SerializerRegistry.getValueSerializerClasses()) {
			if (externalClass.equals(anObject.getClass())) {
				if (!isPrimitive(anObject)) visitedObjects.add(anObject);
				SerializerRegistry.getValueSerializer(externalClass).objectToBuffer(anOutputBuffer, anObject, visitedObjects);
				return;
			}
		}
		
		if (anObject.getClass().isEnum()) {
			SerializerRegistry.getEnumSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
			return;
		} else if (anObject.getClass().isArray()) {
			SerializerRegistry.getArraySerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
			return;
		} else if (RemoteReflectionUtility.isList(anObject.getClass()) ){
			SerializerRegistry.getListPatternSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
			return;
		}
		SerializerRegistry.getBeanSerializer().objectToBuffer(anOutputBuffer, anObject, visitedObjects);
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		if (bufferHeadIsString(anInputBuffer, SerializerRegistry.NULL_HEADER)) {
			removeHeader(anInputBuffer, SerializerRegistry.NULL_HEADER.length());		
			return SerializerRegistry.getNullSerializer().objectFromBuffer(anInputBuffer, null, retrievedObjects);
		}
		
		if (bufferHeadIsString(anInputBuffer, SerializerRegistry.CLASS_HEADER)) {
			removeHeader(anInputBuffer, SerializerRegistry.CLASS_HEADER.length());	
			String classname = (String) objectFromBuffer(anInputBuffer, retrievedObjects);
			Class aClass = null;
			if (classname.startsWith("[")) {
				return SerializerRegistry.getArraySerializer().objectFromBuffer(anInputBuffer, Array.class, retrievedObjects);
			}
			try {
				aClass = Class.forName(classname);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new NotSerializableException("ClassNotFound: " + classname);
			}
			
			if (aClass.isEnum()) {
				return SerializerRegistry.getEnumSerializer().objectFromBuffer(anInputBuffer, aClass, retrievedObjects);
			} else if (RemoteReflectionUtility.isList(aClass) ){
				return SerializerRegistry.getListPatternSerializer().objectFromBuffer(anInputBuffer, aClass, retrievedObjects);
			}
			return SerializerRegistry.getBeanSerializer().objectFromBuffer(anInputBuffer, aClass, retrievedObjects);
		}
		
		if (bufferHeadIsString(anInputBuffer, SerializerRegistry.REFERENCE_HEADER)) {
			removeHeader(anInputBuffer, SerializerRegistry.REFERENCE_HEADER.length());	
			Integer offset = (Integer) SerializerRegistry.getValueSerializer(Integer.class).objectFromBuffer(anInputBuffer, Integer.class, retrievedObjects);	
			if (offset >= 0 && offset < retrievedObjects.size()) {
				return retrievedObjects.get(offset);
			} else {
				throw new StreamCorruptedException("unexpected index:" + offset + " in retrievedObjects of length " + retrievedObjects.size());
			}
		}
		
		for (Class externalClass : SerializerRegistry.getValueSerializerClasses()) {
			if (bufferHeadIsString(anInputBuffer, externalClass.toString())) {
				removeHeader(anInputBuffer, externalClass.toString().length());
				Object obj = SerializerRegistry.getValueSerializer(externalClass).objectFromBuffer(anInputBuffer, externalClass, retrievedObjects);
				if (!isPrimitive(obj)) retrievedObjects.add(obj);
				return obj;
			}
		}
		return null;
	}
	
	private boolean bufferHeadIsString(Object buffer, String header) throws NotSerializableException {
		if (buffer instanceof ByteBuffer) {
			((ByteBuffer)buffer).mark();
			byte[] bytes = new byte[((ByteBuffer)buffer).remaining()];
			((ByteBuffer)buffer).get(bytes);
			((ByteBuffer)buffer).reset();
			String str = new String(bytes);
			return str.startsWith(header);
		} else if (buffer instanceof StringBuffer) {
			return ((StringBuffer)buffer).indexOf(header) == 0;
		} else {
			throw new NotSerializableException("Buffer given unsupported!");
		}
	}
	
	private void removeHeader(Object buffer, int len) throws NotSerializableException {
		if (buffer instanceof ByteBuffer) {
			((ByteBuffer)buffer).get(new byte[len]);
		} else if (buffer instanceof StringBuffer) {
			((StringBuffer)buffer).delete(0, len);
		} else {
			throw new NotSerializableException("Buffer given unsupported!");
		}
	}
	
	private boolean isPrimitive(Object obj) {
		return obj instanceof Integer || obj instanceof Boolean ||
				obj instanceof Float || obj instanceof Double ||
				obj instanceof Short || obj instanceof Long || obj instanceof String;
	}

}
