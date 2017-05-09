package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.IntegerSerializer;
import serialization.valueserializer.StringSerializer;

public class StringSerializerTest {

	private final String test_str = "Test String";
	private final String test_str_empty = "";
	private StringSerializer serializer = new StringSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_str, new ArrayList());
			buffer.flip();
			
			int classLen = String.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, String.class, new ArrayList());
			if (deserializedObject instanceof String) {
				assertEquals(test_str, deserializedObject);
			} else {
				fail("deserializedObject is not an String.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testStringBufferForEmptyString() {
		StringBuffer buffer = new StringBuffer();
		try {
			serializer.objectToBuffer(buffer, test_str_empty, new ArrayList());
			int classLen = String.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, String.class, new ArrayList());
			if (deserializedObject instanceof String) {
				assertEquals(test_str_empty, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an String.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testByteBufferForEmptyString() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_str_empty, new ArrayList());
			buffer.flip();
			
			int classLen = String.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, String.class, new ArrayList());
			if (deserializedObject instanceof String) {
				assertEquals(test_str_empty, deserializedObject);
			} else {
				fail("deserializedObject is not an String.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		try {
			serializer.objectToBuffer(buffer, test_str, new ArrayList());
			int classLen = String.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, String.class, new ArrayList());
			if (deserializedObject instanceof String) {
				assertEquals(test_str, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an String.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
