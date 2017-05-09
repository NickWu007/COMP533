package serialization;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER})
public class LogicalTextualSerializerFactory implements SerializerFactory{
	public Serializer createSerializer() {
		return new LogicalTextualSerializer();
	}
}
