package net.joshdevins.metrics.realtime;

import java.util.List;

import com.yammer.metrics.core.Counter;

/**
 * A realtime wrapper around the core metrics {@link Counter}. Only "write"
 * methods are exposed here. For all other accessors, use {@link #getMetric()}
 * to access the underlying {@link Counter}.
 * 
 * @author Josh Devins
 */
public class RealtimeCounter extends
		AbstractRealtimeMetric<Counter, RealtimeCounterListener> {

	protected RealtimeCounter(final Counter delegate,
			final List<RealtimeCounterListener> listeners) {
		super(delegate, listeners);
	}

	/**
	 * Resets the counter to 0.
	 */
	public void clear() {
		getMetric().clear();
		for (RealtimeCounterListener l : getListeners()) {
			l.onClear(this);
		}
	}

	/**
	 * Decrement the counter by one.
	 */
	public void dec() {
		getMetric().dec();
		for (RealtimeCounterListener l : getListeners()) {
			l.onChange(this, -1);
		}
	}

	/**
	 * Decrement the counter by {@code n}.
	 * 
	 * @param n
	 *            the amount by which the counter will be increased
	 */
	public void dec(final long n) {
		getMetric().dec(n);
		for (RealtimeCounterListener l : getListeners()) {
			l.onChange(this, -n);
		}
	}

	/**
	 * Increment the counter by one.
	 */
	public void inc() {
		getMetric().inc();
		for (RealtimeCounterListener l : getListeners()) {
			l.onChange(this, 1);
		}
	}

	/**
	 * Increment the counter by {@code n}.
	 * 
	 * @param n
	 *            the amount by which the counter will be increased
	 */
	public void inc(final long n) {
		getMetric().inc(n);
		for (RealtimeCounterListener l : getListeners()) {
			l.onChange(this, n);
		}
	}
}
