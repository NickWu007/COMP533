package serialization.typefreeserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.SerializerRegistry;

public class ArraySerializerTest {

	@Test
	public void testByteBuffer() {
		Object[] test_array = {1, "2", 3.0};
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, test_array, new ArrayList());
			buffer.flip();
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject.getClass().isArray()) {
				assertEquals(test_array.length, Array.getLength(deserializedObject));
				for (int i = 0; i < test_array.length; i++) {
					assertEquals(test_array[i], Array.get(deserializedObject, i));
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
		Object[] test_array = {1, "2", 3.0};
		StringBuffer buffer = new StringBuffer();
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, test_array, new ArrayList());
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject.getClass().isArray()) {
				assertEquals(test_array.length, Array.getLength(deserializedObject));
				for (int i = 0; i < test_array.length; i++) {
					assertEquals(test_array[i], Array.get(deserializedObject, i));
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
