package serialization.valueserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import serialization.valueserializer.ShortSerializer;

public class ShortSerializerTest {

	private final Short test_int = 42;
	private ShortSerializer serializer = new ShortSerializer();
	
	@Test
	public void testByteBuffer() {
		ByteBuffer buffer = ByteBuffer.allocate(100);
		try {
			serializer.objectToBuffer(buffer, test_int, new ArrayList());
			buffer.flip();
			
			int classLen = Short.class.toString().length();
			// Get rid of class header
			buffer.get(new byte[classLen]);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Short.class, new ArrayList());
			if (deserializedObject instanceof Short) {
				assertEquals(test_int, deserializedObject);
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
			serializer.objectToBuffer(buffer, test_int, new ArrayList());
			int classLen = Short.class.toString().length();
			// Get rid of class header
			buffer.delete(0, classLen);
			
			Object deserializedObject = serializer.objectFromBuffer(buffer, Short.class, new ArrayList());
			if (deserializedObject instanceof Short) {
				assertEquals(test_int, deserializedObject);
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
