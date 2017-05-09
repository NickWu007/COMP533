package serialization.valueserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.DoubleSerializer;

public class DoubleSerializerTest {

	private final Double test_double = 12.34;
	private DoubleSerializer serializer = new DoubleSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_double, new ArrayList());
			buffer.flip();
			
			int classLen = Double.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Double.class, new ArrayList());
			if (deserializedObject instanceof Double) {
				assertEquals(test_double, deserializedObject);
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
			serializer.objectToBuffer(buffer, test_double, new ArrayList());
			int classLen = Double.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Double.class, new ArrayList());
			if (deserializedObject instanceof Double) {
				assertEquals(test_double, deserializedObject);
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
