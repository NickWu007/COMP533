package serialization;

import util.annotations.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import serialization.typefreeserializer.ArraySerializer;
import serialization.typefreeserializer.BeanSerializer;
import serialization.typefreeserializer.EnumSerializer;
import serialization.typefreeserializer.ListPatternSerializer;
import serialization.valueserializer.ArrayListSerializer;
import serialization.valueserializer.BooleanSerializer;
import serialization.valueserializer.DoubleSerializer;
import serialization.valueserializer.FloatSerializer;
import serialization.valueserializer.HashMapSerializer;
import serialization.valueserializer.HashSetSerializer;
import serialization.valueserializer.HashtableSerializer;
import serialization.valueserializer.IntegerSerializer;
import serialization.valueserializer.LongSerializer;
import serialization.valueserializer.NullSerializer;
import serialization.valueserializer.ShortSerializer;
import serialization.valueserializer.StringSerializer;
import serialization.valueserializer.ValueSerializer;
import serialization.valueserializer.VectorSerializer;
import util.annotations.Comp533Tags;

@Tags({Comp533Tags.SERIALIZER_REGISTRY})
public class SerializerRegistry {

	public static final String DEMIN = ":";
	public static final String REFERENCE_HEADER = "REFERENCE_HEADER";
	public static final String CLASS_HEADER = "CLASS_HEADER";
	public static final String NULL_HEADER = "Null Class";
	private static HashMap externalSerializerMap;
	private static DispatchingSerializer dispatchingSerializer;
	private static NullSerializer nullSerializer;
	private static EnumSerializer enumSerializer;
	private static ArraySerializer arraySerializer;
	private static BeanSerializer beanSerializer;
	private static ListPatternSerializer listSerializer;
	public static int maxClassNameLen = 0;
	
	public static void registerValueSerializer (Class aClass, ValueSerializer anExternalSerializer) {
		if (externalSerializerMap.containsKey(aClass)) {
			externalSerializerMap.remove(aClass);
		}
		if (aClass.toString().length() > maxClassNameLen) maxClassNameLen = aClass.toString().length();
		externalSerializerMap.put(aClass, anExternalSerializer);
	}
	
	public static ArrayList<Class> getValueSerializerClasses() {
		ArrayList<Class> classes = new ArrayList<Class>();
		for (Object obj : externalSerializerMap.keySet()) {
			classes.add((Class) obj);
		}
		return classes;
	}
	
	public static ValueSerializer getValueSerializer(Class aClass) {
		return (ValueSerializer) externalSerializerMap.get(aClass);
	}
	
	public static void registerDeserializingClass(Class class1, Class class2) {
		if (externalSerializerMap.containsKey(class1) && externalSerializerMap.containsKey(class2)) {
			ValueSerializer serializer2 = getValueSerializer(class2);
			externalSerializerMap.remove(class1);
			externalSerializerMap.put(class1, serializer2);
		}
	}
	
	public static void registerDispatchingSerializer (DispatchingSerializer aSispatchingSerializer) {
		dispatchingSerializer = aSispatchingSerializer;
	}
	
	public static DispatchingSerializer getDispatchingSerializer() {
		return dispatchingSerializer;
	}
	
	public static NullSerializer getNullSerializer() {
		return nullSerializer;
	}
	
	public static EnumSerializer getEnumSerializer() {
		return enumSerializer;
	}
	
	public static ArraySerializer getArraySerializer() {
		return arraySerializer;
	}
	
	public static BeanSerializer getBeanSerializer() {
		return beanSerializer;
	}
	
	public static ListPatternSerializer getListPatternSerializer() {
		return listSerializer;
	}
	
	static {
		nullSerializer = new NullSerializer();
		enumSerializer = new EnumSerializer();
		arraySerializer = new ArraySerializer();
		beanSerializer = new BeanSerializer();
		listSerializer =  new ListPatternSerializer();
		registerDispatchingSerializer(new ADispatchingSerializer());
		
		// External serializers
		externalSerializerMap = new HashMap();
		registerValueSerializer(Integer.class, new IntegerSerializer());
		registerValueSerializer(Boolean.class, new BooleanSerializer());
		registerValueSerializer(Double.class, new DoubleSerializer());
		registerValueSerializer(Float.class, new FloatSerializer());
		registerValueSerializer(Long.class, new LongSerializer());
		registerValueSerializer(Short.class, new ShortSerializer());
		registerValueSerializer(String.class, new StringSerializer());
		registerValueSerializer(ArrayList.class, new ArrayListSerializer());
		registerValueSerializer(Vector.class, new VectorSerializer());
		registerValueSerializer(HashSet.class, new HashSetSerializer());
		registerValueSerializer(Hashtable.class, new HashtableSerializer());
		registerValueSerializer(HashMap.class, new HashMapSerializer());
		registerDeserializingClass(ArrayList.class, Vector.class);
	}
}
