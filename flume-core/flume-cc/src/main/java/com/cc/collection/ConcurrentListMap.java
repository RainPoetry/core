package com.cc.collection;

import com.google.common.collect.HashMultimap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: chenchong
 * Date: 2019/2/26
 * description:
 */
public class ConcurrentListMap<K, V> extends ConcurrentHashMap<K, Collection<V>>{


	public ConcurrentListMap(int size) {
		super(size, 0.75f);
	}

	public void putAndCreate(K k, V v) {
		Collection<V> c = getOrCreate(k, new ArrayList<>());
		c.add(v);
	}

	private Collection<V> getOrCreate(K k, Collection<V> c) {
		Collection<V> v;
		if ((v = get(k)) == null) {
			put(k, c);
			v = c;
		}
		return v;
	}

}
