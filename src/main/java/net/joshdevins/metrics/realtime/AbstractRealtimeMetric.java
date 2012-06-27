package net.joshdevins.metrics.realtime;

import java.util.List;

import com.yammer.metrics.core.Metric;

/**
 * A simple base class to help out with managing delegate {@link Metric}s and
 * {@link RealtimeMetricListener}s.
 * 
 * @author Josh Devins
 * 
 * @param <T>
 *            concrete {@link Metric} implementation
 * @param <L>
 *            concrete {@link RealtimeMetricListener} implementation with type
 *            {@code T}
 */
public abstract class AbstractRealtimeMetric<T extends Metric, L extends RealtimeMetricListener<T>> {

	private final T delegate;

	private final List<L> listeners;

	protected AbstractRealtimeMetric(final T delegate, final List<L> listeners) {
		this.delegate = delegate;
		this.listeners = listeners;
	}

	/**
	 * Gets the {@link List} of listeners.
	 */
	protected List<L> getListeners() {
		return listeners;
	}

	/**
	 * Gets the core {@link Metric} that is delegated to.
	 */
	public T getMetric() {
		return delegate;
	}
}
