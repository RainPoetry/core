package rainpoetry.spark.rpc.serialize;

/*
 * User: chenchong
 * Date: 2019/4/2
 * description:
 */

import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface Serializer {

	<T>ByteBuffer serialize(T t);

//	<T>ByteBuffer serialize(OutputStream out);

	<T> T deserialize(ByteBuffer bytes);

}
