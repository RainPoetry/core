package rainpoetry.spark.rpc.serialize;

/*
 * User: chenchong
 * Date: 2019/4/2
 * description:
 */

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffSerialize implements Serializer {

	private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
	private static final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

	@Override
	public <T> ByteBuffer serialize(T t) {
		Class<T> clazz = (Class<T>) t.getClass();
		Schema<T> schema = getSchema(clazz);
		byte[] data;
		try {
			data = ProtostuffIOUtil.toByteArray(t, schema, buffer);
		} finally {
			buffer.clear();
		}
		byte[] names = clazz.getName().getBytes();
		ByteBuffer buffer = ByteBuffer.allocate(1 + data.length + names.length);
		buffer.put((byte) names.length);
		buffer.put(names);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	@Override
	public <T> T deserialize(ByteBuffer bytes) {
		byte length = bytes.get();
		byte[] names = new byte[length];
		bytes.get(names);
		String name = new String(names);
		try {
			Class<T> c = (Class<T>) Class.forName(name);
			Schema<T> schema = getSchema(c);
			T obj = schema.newMessage();
			byte[] datas = new byte[bytes.remaining()];
			bytes.get(datas);
			ProtostuffIOUtil.mergeFrom(datas, obj, schema);
			return obj;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (Objects.isNull(schema)) {
			schema = RuntimeSchema.getSchema(clazz);
			if (Objects.nonNull(schema)) {
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}
}
