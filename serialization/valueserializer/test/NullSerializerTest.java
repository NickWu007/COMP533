package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.SerializerRegistry;

public class NullSerializerTest {

	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, null, new ArrayList());
			buffer.flip();
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject != null) {
				fail("deserializedObject is not null.");
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
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, null, new ArrayList());
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject != null) {
				fail("deserializedObject is not null.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
