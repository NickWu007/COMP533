package serialization.valueserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.IntegerSerializer;

public class IntegerSerializerTest {

	private final Integer test_int = 42;
	private IntegerSerializer serializer = new IntegerSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_int, new ArrayList());
			buffer.flip();
			
			int classLen = Integer.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Integer.class, new ArrayList());
			if (deserializedObject instanceof Integer) {
				assertEquals(test_int, deserializedObject);
			} else {
				fail("deserializedObject is not an Integer.");
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
			serializer.objectToBuffer(buffer, test_int, new ArrayList());
			int classLen = Integer.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Integer.class, new ArrayList());
			if (deserializedObject instanceof Integer) {
				assertEquals(test_int, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an Integer.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
