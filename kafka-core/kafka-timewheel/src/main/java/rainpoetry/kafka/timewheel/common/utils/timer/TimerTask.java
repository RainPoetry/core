package rainpoetry.kafka.timewheel.common.utils.timer;


import rainpoetry.kafka.timewheel.common.utils.Logging;

/**
 * User: chenchong
 * Date: 2019/1/18
 * description:
 */
public abstract class TimerTask extends Logging implements Runnable {

	private TimerTaskEntry timerTaskEntry = null;
	// TimeUnit.MILLISECONDS
	protected long delayMs;

	public void cancel() {
		synchronized (this) {
			if (timerTaskEntry != null)
				timerTaskEntry.remove();
			timerTaskEntry = null;
		}
	}

	public void setTimerTaskEntry(TimerTaskEntry entry) {
		synchronized (this) {
			if (timerTaskEntry != null && timerTaskEntry != entry)
				timerTaskEntry.remove();
			timerTaskEntry = entry;
		}
	}

	public TimerTaskEntry getTimerTaskEntry() {
		return timerTaskEntry;
	}

	public long delayMs() {
		return delayMs;
	}

}
