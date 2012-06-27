package net.joshdevins.metrics.realtime;

import java.util.List;

import com.yammer.metrics.core.Meter;

public class RealtimeMeter extends
		AbstractRealtimeMetric<Meter, RealtimeMeterListener> {

	protected RealtimeMeter(final Meter delegate,
			final List<RealtimeMeterListener> listeners) {
		super(delegate, listeners);
	}

	/**
	 * Mark the occurrence of a given number of events.
	 * 
	 * @param n
	 *            the number of events
	 */
	public void mark(final long n) {
		getMetric().mark(n);
		for (RealtimeMeterListener l : getListeners()) {
			l.onMark(this, n);
		}
	}
}
