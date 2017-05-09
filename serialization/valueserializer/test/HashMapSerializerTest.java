package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.junit.Test;

import serialization.SerializerRegistry;

public class HashMapSerializerTest {

	private HashMap list = new HashMap();
	
	@Test
	public void testByteBuffer() {
		Integer a1 = 42;
		Boolean a2 = true;
		Short a3 = 12;
		Long a4 = 123546789L;
		Double a5 = 12.34;
		Float a6 = (float) 132.04;
		String a7 = "hello world!";
		list.put(a1, a2);
		list.put(a3, a4);
		list.put(a5, a6);
		list.put(a6, a7);
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, new ArrayList());
			buffer.flip();
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof HashMap) {
				HashMap deserializedList = (HashMap)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (Object key : deserializedList.keySet()) {
					assertTrue(list.containsKey(key));
					Object value = deserializedList.get(key);
					assertTrue(list.get(key).equals(value));
				}
			} else {
				fail("deserializedObject is not a HashMap.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testStringBuffer() {
		Integer a1 = 42;
		Boolean a2 = true;
		Short a3 = 12;
		Long a4 = 123546789L;
		Double a5 = 12.34;
		Float a6 = (float) 132.04;
		String a7 = "hello world!";
		list.put(a1, a2);
		list.put(a3, a4);
		list.put(a5, a6);
		list.put(a6, a7);
		StringBuffer buffer = new StringBuffer();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, new ArrayList());
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof HashMap) {
				HashMap deserializedList = (HashMap)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (Object key : deserializedList.keySet()) {
					assertTrue(list.containsKey(key));
					Object value = deserializedList.get(key);
					assertTrue(list.get(key).equals(value));
				}
			} else {
				fail("deserializedObject is not a HashMap.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
