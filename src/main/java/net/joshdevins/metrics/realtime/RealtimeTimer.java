package net.joshdevins.metrics.realtime;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.core.Timer;

public class RealtimeTimer extends
		AbstractRealtimeMetric<Timer, RealtimeTimerListener> {

	protected RealtimeTimer(final Timer delegate,
			final List<RealtimeTimerListener> listeners) {
		super(delegate, listeners);
	}

	/**
	 * Adds a recorded duration.
	 * 
	 * @param duration
	 *            the length of the duration
	 * @param unit
	 *            the scale unit of {@code duration}
	 */
	public void update(final long duration, final TimeUnit unit) {
		getMetric().update(duration, unit);
		for (RealtimeTimerListener l : getListeners()) {
			l.onUpdate(this, duration, unit);
		}
	}
}
