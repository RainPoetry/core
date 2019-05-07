package com.guava.cc.sink;

import com.guava.cc.config.AbstractConfig;
import com.guava.cc.config.ConfigDef;
import com.guava.cc.config.ConfigDef.Type;
import com.guava.cc.config.validate.NonEmptyString;
import com.guava.cc.config.validate.Range;
import com.guava.cc.config.validate.ValidString;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;

/**
 * User: chenchong
 * Date: 2019/2/28
 * description:
 */
public class KafkaSinkConfig extends AbstractConfig {

	private static final ConfigDef CONFIG;

	public static final String SERVER = "bootstrap.servers";
	public static final String TOPIC = "topic";
	public static final String ACK = "acks";
	private static final String ACK_DEFAULT = "all";
	public static final String RETRY = "retries";
	private static final int RETRY_DEFAULT = 200;
	public static final String BATCH_SIZE = "batch.size";
	private static final int BATCH_SIZE_DEFAULT = 16384;
	public static final String LINGER_MS = "linger.ms";
	private static final int LINGER_MS_DEFAULT = 100;
	public static final String KEY_SERIALIZER = "key.serializer";
	private static final String KEY_SERIALIZER_DEFAULT = "org.apache.kafka.common.serialization.StringSerializer";
	public static final String VALUE_SERIALIZER = "value.serializer";
	private static final Object VALUE_SERIALIZER_DEFAULT = "org.apache.kafka.common.serialization.StringSerializer";



	static {
		CONFIG = new ConfigDef()
				.define(SERVER, Type.STRING,new NonEmptyString())
				.define(TOPIC,Type.STRING,new NonEmptyString())
				.define(ACK,Type.STRING,ACK_DEFAULT, ValidString.in("all", "-1", "0", "1"))
				.define(RETRY,Type.INT,RETRY_DEFAULT, Range.atLeast(0))
				.define(BATCH_SIZE,Type.INT,BATCH_SIZE_DEFAULT,Range.atLeast(0))
				.define(LINGER_MS,Type.INT,LINGER_MS_DEFAULT,Range.atLeast(0))
				.define(KEY_SERIALIZER,Type.CLASS,KEY_SERIALIZER_DEFAULT,null)
				.define(VALUE_SERIALIZER, Type.CLASS,VALUE_SERIALIZER_DEFAULT,null);
	}

	public KafkaSinkConfig(Map<?, ?> originals) {
		super(CONFIG, originals);
	}
}
