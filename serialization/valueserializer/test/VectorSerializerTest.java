package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Vector;

import org.junit.Test;

import serialization.SerializerRegistry;

public class VectorSerializerTest {

private Vector list = new Vector();
	
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
			if (deserializedObject instanceof Vector) {
				Vector deserializedList = (Vector)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (int i = 0; i < list.size(); i++) {
					assertEquals(list.get(i), deserializedList.get(i));
				}
			} else {
				fail("deserializedObject is not a Vector.");
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
			if (deserializedObject instanceof Vector) {
				Vector deserializedList = (Vector)deserializedObject;
				assertEquals(list.size(), deserializedList.size());
				for (int i = 0; i < list.size(); i++) {
					assertEquals(list.get(i), deserializedList.get(i));
				}
			} else {
				fail("deserializedObject is not an Vector.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
