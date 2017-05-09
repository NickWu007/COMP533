package serialization.typefreeserializer.test;

import static org.junit.Assert.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.SerializerRegistry;

public class EnumSerializerTest {

	enum TestEnum {
		A, B, C;
	}
	
	@Test
	public void testByteBuffer() {
		TestEnum testEnum = TestEnum.A;
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, testEnum, new ArrayList());
			buffer.flip();
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof TestEnum) {
				assertEquals(testEnum, deserializedObject);
			} else {
				fail("deserializedObject is not an TestEnum.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testStringBuffer() {
		TestEnum testEnum = TestEnum.A;
		StringBuffer buffer = new StringBuffer();
		
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, testEnum, new ArrayList());
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof TestEnum) {
				assertEquals(testEnum, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an TestEnum.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
