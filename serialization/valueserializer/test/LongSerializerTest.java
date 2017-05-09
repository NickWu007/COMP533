package serialization.valueserializer.test;

import static org.junit.Assert.*;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.IntegerSerializer;
import serialization.valueserializer.LongSerializer;

public class LongSerializerTest {

	private final Long test_long = 2147483649L;
	private LongSerializer serializer = new LongSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_long, new ArrayList());
			buffer.flip();
			
			int classLen = Long.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Long.class, new ArrayList());
			if (deserializedObject instanceof Long) {
				assertEquals(test_long, deserializedObject);
			} else {
				fail("deserializedObject is not an Integer.");
			}
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exceptions thrown");
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exceptions thrown");
		}
	}
	
	@Test
	public void testStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		try {
			serializer.objectToBuffer(buffer, test_long, new ArrayList());
			int classLen = Long.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Long.class, new ArrayList());
			if (deserializedObject instanceof Long) {
				assertEquals(test_long, deserializedObject);
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an Integer.");
			}
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exceptions thrown");
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Exceptions thrown");
		}
	}

}
