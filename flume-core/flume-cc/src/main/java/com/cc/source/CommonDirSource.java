package com.cc.source;

import com.cc.collect.AbstractCollector;
import com.cc.collect.Collector;
import com.cc.collect.CollectionListener;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.JSONEvent;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public class CommonDirSource extends BaseSource implements Configurable,EventDrivenSource {

	private Collector<String> collector;
	private CommonDirSourceConfig sourceConfig;

	@Override
	public void run() {
		info("start");
		// 注册监听器
		collector.register(new CollectionListener() {
				public void onCompleteBatch(java.util.Collection<String> t) {
					List<Event> events = new ArrayList<>();
					for(String s : t) {
						Event event = new JSONEvent();
						event.setBody(s.getBytes());
						events.add(event);
					}
					getChannelProcessor().processEventBatch(events);
				}

			@Override
			public void onComplete(String t) {
				Event event = new JSONEvent();
				event.setBody(t.getBytes());
				getChannelProcessor().processEvent(event);
			}
		});
		collector.poll();
	}

	@Override
	public void close() {
		info("close");
		collector.interrupt();
	}

	@Override
	public void configure(Context context) {
		Map<String, String> parameters = context.getParameters();
		sourceConfig = new CommonDirSourceConfig(parameters);
		try {
			collector = AbstractCollector.Builder.build(sourceConfig);
		} catch (UnknownHostException e) {
			logger.error("host unknown: " + e.getMessage());
			System.exit(1);
		}
	}

	@Override
	protected String ident() {
		return "CommonDirSource";
	}

}