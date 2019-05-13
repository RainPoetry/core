package rainpoetry.livy.repl.bean;

/*
 * User: chenchong
 * Date: 2019/5/13
 * description:
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatementState {

	Waiting("waiting"),
	Running("running"),
	Available("available"),
	Cancelling("cancelling"),
	Cancelled("cancelled");

	private static final Logger LOG = LoggerFactory.getLogger(StatementState.class);

	private final String state;

	StatementState(final String text) {
		this.state = text;
	}

	@JsonValue
	@Override
	public String toString() {
		return state;
	}

	public boolean isOneOf(StatementState... states) {
		for (StatementState s : states) {
			if (s == this) {
				return true;
			}
		}
		return false;
	}

	private static final Map<StatementState, List<StatementState>> PREDECESSORS;

	static void put(StatementState key,
					Map<StatementState, List<StatementState>> map,
					StatementState... values) {
		map.put(key, Collections.unmodifiableList(Arrays.asList(values)));
	}

	static {
		final Map<StatementState, List<StatementState>> predecessors =
				new EnumMap<>(StatementState.class);
		put(Waiting, predecessors);
		put(Running, predecessors, Waiting);
		put(Available, predecessors, Running);
		put(Cancelling, predecessors, Running);
		put(Cancelled, predecessors, Waiting, Cancelling);

		PREDECESSORS = Collections.unmodifiableMap(predecessors);
	}

	static boolean isValid(StatementState from, StatementState to) {
		return PREDECESSORS.get(to).contains(from);
	}

	static void validate(StatementState from, StatementState to) {
		LOG.debug("{} -> {}", from, to);
		if (!isValid(from, to)) {
			throw new IllegalStateException("Illegal Transition: " + from + " -> " + to);
		}
	}
}
