package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.MeterListener;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.Stoppable;

/**
 * A basic foundation for a StatsD {@link MeterListener}. Note that this does
 * not implement {@link Stoppable} since the underlying {@link StatsDConnection}
 * is shared amongst many {@link StatsDClient}s and listeners so it should be
 * stopped once at the end and not on a per-listener basis.
 * 
 * @author Josh Devins
 */
public abstract class AbstractStatsDListener {

	private final StatsDClient client;

	private MetricPredicate metricPredicate;

	/**
	 * Default constructor.
	 */
	protected <M extends Metric> AbstractStatsDListener(
			final Class<M> metricType, final StatsDClient client) {

		this.client = client;
		metricPredicate = new MetricPredicate() {
			public boolean matches(final MetricName name, final Metric metric) {
				return metricType.isAssignableFrom(metric.getClass());
			}
		};
	}

	/**
	 * Returns either the default metric predicate based on the observed metric,
	 * or can be overriden by {@link #setMetricPredicate(MetricPredicate)}.
	 */
	public MetricPredicate getMetricPredicate() {
		return metricPredicate;
	}

	/**
	 * Allow overriding of the default {@link MetricPredicate}.
	 */
	public void setMetricPredicate(final MetricPredicate metricPredicate) {
		this.metricPredicate = metricPredicate;
	}

	/**
	 * Gets the underlying {@link StatsDClient}.
	 */
	protected StatsDClient getClient() {
		return client;
	}
}
