package serialization.typefreeserializer.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.junit.Test;

import examples.serialization.ABMISpreadsheet;
import examples.serialization.BMISpreadsheet;
import serialization.SerializerRegistry;

public class BeanSerializerTest {

	@Test
	public void testStringBuffer() {
		BMISpreadsheet bmi = new ABMISpreadsheet();
		StringBuffer buffer = new StringBuffer();
		bmi.setHeight(1.91);
		bmi.setMale(false);
		bmi.setWeight(80);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, bmi, new ArrayList());
			
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof BMISpreadsheet) {
				BMISpreadsheet deserializedBmi = (BMISpreadsheet) deserializedObject;
				assert(bmi.getHeight() == deserializedBmi.getHeight());
				assert(bmi.getWeight() == deserializedBmi.getWeight());
				assert(bmi.getBMI() == deserializedBmi.getBMI());
				assert(bmi.isMale() == deserializedBmi.isMale());
				assertEquals(buffer.length(), 0);
			} else {
				fail("deserializedObject is not an TestEnum.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}
	
	@Test
	public void testByteBuffer() {
		BMISpreadsheet bmi = new ABMISpreadsheet();
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		bmi.setHeight(1.91);
		bmi.setMale(false);
		bmi.setWeight(80);
		try {
			SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, bmi, new ArrayList());
			buffer.flip();
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			if (deserializedObject instanceof BMISpreadsheet) {
				BMISpreadsheet deserializedBmi = (BMISpreadsheet) deserializedObject;
				assert(bmi.getHeight() == deserializedBmi.getHeight());
				assert(bmi.getWeight() == deserializedBmi.getWeight());
				assert(bmi.getBMI() == deserializedBmi.getBMI());
				assert(bmi.isMale() == deserializedBmi.isMale());
			} else {
				fail("deserializedObject is not an TestEnum.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Exceptions thrown");
		} 
	}

}
