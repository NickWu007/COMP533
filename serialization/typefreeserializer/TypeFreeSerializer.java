package serialization.typefreeserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public interface TypeFreeSerializer {
	void objectToBuffer (Object anOutputBuffer, Object anObject, ArrayList visitedObjects) throws NotSerializableException;
	 
	Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects) throws StreamCorruptedException, NotSerializableException;
}
