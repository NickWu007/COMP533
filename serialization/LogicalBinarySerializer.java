package serialization;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_BINARY_SERIALIZER})
public class LogicalBinarySerializer implements Serializer{

	@Override
	public Object objectFromInputBuffer(ByteBuffer inputBuffer) throws StreamCorruptedException {
		try {
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(inputBuffer, new ArrayList());
			inputBuffer.flip();
			return deserializedObject;
		} catch (NotSerializableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ByteBuffer outputBufferFromObject(Object object) throws NotSerializableException {
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, object, new ArrayList());
		buffer.flip();
		return buffer;
	}

}
