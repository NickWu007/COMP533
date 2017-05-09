package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import serialization.SerializerRegistry;

public class HashSetSerializerTest {

	private HashSet list = new HashSet();
	
	@Test
	public void testByteBuffer() {
		Integer a1 = 42;
		Boolean a2 = true;
		Short a3 = 12;
		Long a4 = 123546789L;
		Double a5 = 12.34;
		Float a6 = (float) 132.04;
		String a7 = "hello world!";
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		list.add(a6);
		list.add(a7);
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, new ArrayList());
			buffer.flip();
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof HashSet) {
				HashSet deserializedList = (HashSet)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (Object obj : deserializedList) {
					assertTrue(list.contains(obj));
				}
			} else {
				fail("deserializedObject is not a HashSet.");
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
		list.add(a1);
		list.add(a2);
		list.add(a3);
		list.add(a4);
		list.add(a5);
		list.add(a6);
		list.add(a7);
		StringBuffer buffer = new StringBuffer();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, new ArrayList());
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof HashSet) {
				HashSet deserializedList = (HashSet)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (Object obj : deserializedList) {
					assertTrue(list.contains(obj));
				}
			} else {
				fail("deserializedObject is not a HashSet.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
