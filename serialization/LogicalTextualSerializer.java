package serialization;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER})
public class LogicalTextualSerializer implements Serializer{

	@Override
	public Object objectFromInputBuffer(ByteBuffer inputBuffer) throws StreamCorruptedException {
		byte[] remaining = new byte[inputBuffer.remaining()];
		inputBuffer.mark();
		inputBuffer.get(remaining);
		StringBuffer buffer = new StringBuffer(new String(remaining));
		try {
			Object deserializedObject = SerializerRegistry.getDispatchingSerializer().objectFromBuffer(buffer, new ArrayList());
			int read = remaining.length - buffer.length();
			inputBuffer.reset();
			inputBuffer.get(new byte[read]);
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
		StringBuffer buffer = new StringBuffer();
		ArrayList visitedObjects = new ArrayList();
		SerializerRegistry.getDispatchingSerializer().objectToBuffer(buffer, object, visitedObjects);
		return ByteBuffer.wrap(buffer.toString().getBytes());
	}

}
