package com.cc.collect;

import com.google.common.eventbus.Subscribe;

import java.util.Collection;
import java.util.List;

/**
 * User: chenchong
 * Date: 2019/2/25
 * description:
 */
public interface CollectionListener {


	void onCompleteBatch(Collection<String> t);


	void onComplete(String t);

}

class CommonListener {

	CollectionListener listener;

	public CommonListener(CollectionListener listener) {
		this.listener = listener;
	}
	@Subscribe
	public void onCompleteBatch(Collection<String> t) {
		listener.onCompleteBatch(t);;
	}

	@Subscribe
	public void onComplete(String t) {
		listener.onComplete(t);
	}


}
