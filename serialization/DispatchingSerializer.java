package serialization;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.DISPATCHING_SERIALIZER})
public interface DispatchingSerializer {

	void objectToBuffer (Object anOutputBuffer, Object anObject, ArrayList visitedObjects) throws NotSerializableException;
	 
	Object objectFromBuffer(Object anInputBuffer, ArrayList retrievedObjects) throws StreamCorruptedException, NotSerializableException;
	
}
