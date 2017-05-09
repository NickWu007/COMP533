package serialization.typefreeserializer;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import port.trace.serialization.extensible.ExtensibleBufferDeserializationFinished;
import port.trace.serialization.extensible.ExtensibleBufferDeserializationInitiated;
import port.trace.serialization.extensible.ExtensibleValueSerializationFinished;
import port.trace.serialization.extensible.ExtensibleValueSerializationInitiated;
import serialization.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.misc.RemoteReflectionUtility;

@Tags({Comp533Tags.BEAN_SERIALIZER})
public class BeanSerializer implements TypeFreeSerializer{

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, ArrayList visitedObjects)
			throws NotSerializableException {
		if (!(anObject instanceof Serializable)) {
			throw new NotSerializableException(anObject.toString() + " is not a Serializable instance.");
		}
		try {
			ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
			PropertyDescriptor[] descriptors = Introspector.getBeanInfo(anObject.getClass()).getPropertyDescriptors();
			if (anOutputBuffer instanceof ByteBuffer) {
				((ByteBuffer)anOutputBuffer).put(SerializerRegistry.CLASS_HEADER.getBytes());
			} else if (anOutputBuffer instanceof StringBuffer) {
				((StringBuffer)anOutputBuffer).append(SerializerRegistry.CLASS_HEADER);
			} else {
				throw new NotSerializableException("Buffer given unsupported!");
			}
			visitedObjects.add(anObject);
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, anObject.getClass().getName(), visitedObjects);
			ArrayList beans = new ArrayList();
			for (PropertyDescriptor des : descriptors) {
				if (des.getReadMethod() == null || des.getWriteMethod() == null) { 
//					System.out.println("Property " + des.getName() + " missing read or write method");
				} else {
					if (RemoteReflectionUtility.isTransient(des.getReadMethod())) {
//						System.out.println("Property " + des.getName() + " is transient");
					} else {
						Object value = des.getReadMethod().invoke(anObject, null);
						beans.add(des.getName());
						beans.add(value);
					}
				}
			}
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, beans.size(), visitedObjects);
			for (int i = 0; i < beans.size(); i += 2) {
				SerializerRegistry.getValueSerializer(String.class).objectToBuffer(anOutputBuffer, beans.get(i), visitedObjects);
				SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, beans.get(i+1), visitedObjects);
			}
			ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		ExtensibleBufferDeserializationInitiated.newCase(this, null, anInputBuffer, aClass);
		Integer length = (Integer) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
		try {
			PropertyDescriptor[] descriptors = Introspector.getBeanInfo(aClass).getPropertyDescriptors();
			HashMap<String, PropertyDescriptor> desMap = new HashMap<String, PropertyDescriptor>();
			for (PropertyDescriptor des : descriptors) {
				desMap.put(des.getName(), des);
			}
			
			Object value = aClass.newInstance();
			retrievedObjects.add(value);
			for (int i = 0; i < length; i+=2) {
				String propertyName = (String) SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				Object propertyValue = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(anInputBuffer, retrievedObjects);
				PropertyDescriptor property = desMap.get(propertyName);
				if (property != null) {
					property.getWriteMethod().invoke(value, propertyValue);
				}
			}
			
			RemoteReflectionUtility.invokeInitSerializedObject(value);
			ExtensibleBufferDeserializationFinished.newCase(this, null, anInputBuffer, value, retrievedObjects);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
		
	}

}
