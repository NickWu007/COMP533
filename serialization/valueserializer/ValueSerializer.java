package serialization.valueserializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashSet;

import util.annotations.Tags;
import util.annotations.Comp533Tags;

@Tags({Comp533Tags.VALUE_SERIALIZER})
public interface ValueSerializer {
	void objectToBuffer (Object anOutputBuffer, Object anObject, ArrayList visitedObjects) throws NotSerializableException;
 
	Object objectFromBuffer(Object anInputBuffer, Class aClass, ArrayList retrievedObjects) throws StreamCorruptedException, NotSerializableException;
}
