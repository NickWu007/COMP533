package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.FloatSerializer;
import serialization.valueserializer.IntegerSerializer;

public class FloatSerializerTest {

	private final Float test_float = (float) 12.345;
	private FloatSerializer serializer = new FloatSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_float, new ArrayList());
			buffer.flip();
			
			int classLen = Float.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Float.class, new ArrayList());
			if (deserializedObject instanceof Float) {
				assertEquals(test_float, deserializedObject);
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
			serializer.objectToBuffer(buffer, test_float, new ArrayList());
			int classLen = Float.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Float.class, new ArrayList());
			if (deserializedObject instanceof Float) {
				assertEquals(test_float, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an Float.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
