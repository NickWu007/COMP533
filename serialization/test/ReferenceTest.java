package serialization.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import serialization.SerializerRegistry;

public class ReferenceTest {

	private ArrayList list = new ArrayList();
	
	@Test
	public void testStringBuffer() {
		Integer a1 = 42;
		Boolean a2 = true;
		list.add(a1);
		list.add(a2);
		list.add(a1);
		list.add(list);
		StringBuffer buffer = new StringBuffer();
		ArrayList visitedObjects = new ArrayList();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, visitedObjects);
			System.out.println(buffer.toString());
			System.out.println("visitedObjects: " + visitedObjects);
			visitedObjects = new ArrayList();
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, visitedObjects);
			System.out.println("retrievedObjects: " + visitedObjects);
			if (deserializedObject instanceof ArrayList) {
				ArrayList deserializedList = (ArrayList)deserializedObject;
				assertEquals(list.toString(), deserializedList.toString());
			} else {
				fail("deserializedObject is not an ArrayList.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
//	@Test
//	public void testByteBuffer() {
//		Integer a1 = 42;
//		Boolean a2 = true;
//		list.add(a1);
//		list.add(a2);
//		list.add(a1);
//		list.add(list);
//		StringBuffer buffer = new StringBuffer();
//		ArrayList visitedObjects = new ArrayList();
//		try {
//			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, list, visitedObjects);
//			System.out.println(buffer.toString());
//			System.out.println("visitedObjects: " + visitedObjects);
//			visitedObjects = new ArrayList();
//			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, visitedObjects);
//			System.out.println("retrievedObjects: " + visitedObjects);
//			if (deserializedObject instanceof ArrayList) {
//				ArrayList deserializedList = (ArrayList)deserializedObject;
//				assertEquals(list.toString(), deserializedList.toString());
//			} else {
//				fail("deserializedObject is not an ArrayList.");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			fail("Exceptions thrown");
//		} 
//	}

}
