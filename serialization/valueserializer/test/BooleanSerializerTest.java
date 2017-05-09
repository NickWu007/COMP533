package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.BooleanSerializer;
import serialization.valueserializer.IntegerSerializer;

public class BooleanSerializerTest {

	private final Boolean test_bool = false;
	private BooleanSerializer serializer = new BooleanSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_bool, new ArrayList());
			buffer.flip();
			
			int classLen = Boolean.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Boolean.class, new ArrayList());
			if (deserializedObject instanceof Boolean) {
				assertEquals(test_bool, deserializedObject);
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
			serializer.objectToBuffer(buffer, test_bool, new ArrayList());
			int classLen = Boolean.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			Object deserializedObject = serializer.objectFromBuffer(buffer, Boolean.class, new ArrayList());
			if (deserializedObject instanceof Boolean) {
				assertEquals(test_bool, deserializedObject);
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
