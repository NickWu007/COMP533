package serialization;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_BINARY_SERIALIZER})
public class LogicalBinarySerializerFactory implements SerializerFactory{
	public Serializer createSerializer() {
		return new LogicalBinarySerializer();
	}

}
