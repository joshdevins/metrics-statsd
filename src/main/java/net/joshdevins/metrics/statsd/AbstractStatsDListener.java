package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.MeterListener;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.ObservableMetric;
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

	private BucketNameFormatter bucketNameFormatter;

	private MetricPredicate metricPredicate;

	/**
	 * Default constructor.
	 */
	protected <M extends Metric> AbstractStatsDListener(
			final Class<M> metricType, final StatsDClient client) {

		this.client = client;

		bucketNameFormatter = new DefaultBucketNameFormatter();
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
	 * Allow overriding of the default {@link BucketNameFormatter} (which is a
	 * {@link DefaultBucketNameFormatter}.
	 */
	public void setBucketNameFormatter(
			final BucketNameFormatter bucketNameFormatter) {

		if (bucketNameFormatter == null) {
			throw new NullPointerException();
		}

		this.bucketNameFormatter = bucketNameFormatter;
	}

	/**
	 * Allow overriding of the default {@link MetricPredicate} (which is based
	 * on the class of the listener).
	 */
	public void setMetricPredicate(final MetricPredicate metricPredicate) {

		if (metricPredicate == null) {
			throw new NullPointerException();
		}

		this.metricPredicate = metricPredicate;
	}

	protected String extractBucketName(final ObservableMetric<?> metric) {
		return bucketNameFormatter.format(metric.getName());
	}

	/**
	 * Gets the underlying {@link StatsDClient}.
	 */
	protected StatsDClient getClient() {
		return client;
	}
}
