package net.joshdevins.metrics.realtime;

import java.util.List;

import com.yammer.metrics.core.Histogram;

public class RealtimeHistogram extends
		AbstractRealtimeMetric<Histogram, RealtimeHistogramListener> {

	protected RealtimeHistogram(final Histogram delegate,
			final List<RealtimeHistogramListener> listeners) {
		super(delegate, listeners);
	}

	/**
	 * Clears all recorded values.
	 */
	public void clear() {
		getMetric().clear();
		for (RealtimeHistogramListener l : getListeners()) {
			l.onClear(this);
		}
	}

	/**
	 * Adds a recorded value.
	 */
	public void update(final long value) {
		getMetric().update(value);
		for (RealtimeHistogramListener l : getListeners()) {
			l.onUpdate(this, value);
		}
	}
}
