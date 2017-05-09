package serialization.typefreeserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import examples.serialization.AStringHistory;
import examples.serialization.StringHistory;
import serialization.SerializerRegistry;
import util.misc.RemoteReflectionUtility;

public class ListPatternSerializerTest {
	
	@Test
	public void testByteBuffer() {
		StringHistory stringHistory = new AStringHistory();
		stringHistory.add("James Dean");
		stringHistory.add("Joe Doe");
		stringHistory.add("Jane Smith");
		stringHistory.add("John Smith");
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, stringHistory, new ArrayList());
			buffer.flip();
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (RemoteReflectionUtility.isList(deserializedObject.getClass())) {
				StringHistory deserializedHistory = (StringHistory) deserializedObject;
				assertEquals(stringHistory.size(), deserializedHistory.size());
				for (int i = 0; i < stringHistory.size(); i++) {
					assertEquals(stringHistory.get(i), deserializedHistory.get(i));
				}
			} else {
				fail("deserializedObject is not an Array.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

	@Test
	public void testStringBuffer() {
		StringHistory stringHistory = new AStringHistory();
		stringHistory.add("James Dean");
		stringHistory.add("Joe Doe");
		stringHistory.add("Jane Smith");
		stringHistory.add("John Smith");
		StringBuffer buffer = new StringBuffer();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, stringHistory, new ArrayList());
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (RemoteReflectionUtility.isList(deserializedObject.getClass())) {
				StringHistory deserializedHistory = (StringHistory) deserializedObject;
				assertEquals(stringHistory.size(), deserializedHistory.size());
				for (int i = 0; i < stringHistory.size(); i++) {
					assertEquals(stringHistory.get(i), deserializedHistory.get(i));
				}
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an Array.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
}
