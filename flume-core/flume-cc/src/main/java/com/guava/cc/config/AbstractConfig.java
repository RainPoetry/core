package com.guava.cc.config;


import com.guava.cc.exception.ConfigException;
import com.guava.cc.utils.Utils;

import java.util.Collections;
import java.util.Map;

/**
 * User: chenchong
 * Date: 2018/11/16
 * description:
 */
public class AbstractConfig {

	/* the original values passed in by the user */
	private final Map<String, ?> originals;

	/* the parsed values */
	private final Map<String, Object> values;

	private final ConfigDef definition;

	public AbstractConfig(ConfigDef definition, Map<?, ?> originals) {
		/* check that all the keys are really strings */
		for (Map.Entry<?, ?> entry : originals.entrySet())
			if (!(entry.getKey() instanceof String))
				throw new ConfigException(entry.getKey().toString(), entry.getValue(), "Key must be a string.");
		this.originals = (Map<String, ?>) originals;
		this.values = definition.parse(this.originals);
		this.definition = definition;
	}

	public Object get(String key) {
		if (!values.containsKey(key))
			throw new ConfigException(String.format("Unknown configuration '%s'", key));
		return values.get(key);
	}

	public String getString(String key) {
		return (String) get(key);
	}
	public Class<?> getClass(String key) {
		return (Class<?>) get(key);
	}

	public <T> T getConfiguredInstance(String key, Class<T> t) {
		Class<?> c = getClass(key);
		if (c == null)
			return null;
		Object o = Utils.newInstance(c);
		if (!t.isInstance(o))
			throw new ConfigException(c.getName() + " is not an instance of " + t.getName());
		return t.cast(o);
	}

	public Map<String,Object> toMap() {
		return Collections.unmodifiableMap(values);
	}
}


