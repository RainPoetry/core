package com.guava.cc.sink;

import com.guava.cc.config.CommonConfig;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.kafka.clients.producer.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * User: chenchong
 * Date: 2019/2/28
 * description:
 */
public class KafkaSink extends BaseSink implements Configurable{

	private KafkaSinkConfig sinkConfig;

	private Producer producer;
	private String topic;
//	private List<Future<RecordMetadata>> inFlightSending ;

	@Override
	public Status process() throws EventDeliveryException {
		Status result = Status.READY;
		Channel channel = getChannel();
		Transaction transaction = channel.getTransaction();
		Event event = null;
		try{
			transaction.begin();
			event = channel.take();
			if (event != null) {
				String key = null;
				if (event.getHeaders() != null)
					key = event.getHeaders().get(CommonConfig.KAFKA_KEY);
				Future<RecordMetadata> future;
				if (key != null)
					producer.send(new ProducerRecord(topic,key,new String(event.getBody())), new KafkaCallable());
				else
					producer.send(new ProducerRecord(topic,new String(event.getBody())), new KafkaCallable());
//				if (future!=null)
//					inFlightSending.add(future);
			} else {
				result = Status.BACKOFF;
			}
			transaction.commit();
		}catch (Exception ex) {
			transaction.rollback();
			throw new EventDeliveryException("Failed to log event: " + event, ex);
		} finally {
			transaction.close();
		}
		return result;
	}

	@Override
	public void configure(Context context) {
		Map<String, String> parameters = context.getParameters();
		sinkConfig = new KafkaSinkConfig(parameters);
	}

	// 初始化环境
	@Override
	public void run() {
		this.producer = new KafkaProducer(sinkConfig.toMap());
		this.topic = (String)sinkConfig.get(KafkaSinkConfig.TOPIC);
//		this.inFlightSending = new ArrayList<>();
	}

	@Override
	protected String ident() {
		return "kafka-sink";
	}

	private class KafkaCallable implements Callback {

		@Override
		public void onCompletion(RecordMetadata recordMetadata, Exception e) {
			if (e != null)
				info("kafka produce error: " + e.getMessage());
		}
	}
}
