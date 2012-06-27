package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.Metric;

/**
 * Building block for metrics that can be sampled.
 * 
 * @author Josh Devins
 */
public abstract class AbstractSamplingStatsDListener extends
		AbstractStatsDListener {

	private double sampleRate;

	/**
	 * Default constructor.
	 */
	protected <M extends Metric> AbstractSamplingStatsDListener(
			final Class<M> metricType, final StatsDClient client) {
		this(metricType, client, 1.0);
	}

	/**
	 * Default constructor with sampling rate injection.
	 */
	protected <M extends Metric> AbstractSamplingStatsDListener(
			final Class<M> metricType, final StatsDClient client,
			final double sampleRate) {
		super(metricType, client);

		this.sampleRate = sampleRate;
	}

	public double getSampleRate() {
		return sampleRate;
	}

	public void setSampleRate(final double sampleRate) {
		this.sampleRate = sampleRate;
	}

	/**
	 * Sampling should only happen if the sample rate is between 0.0 and 1.0.
	 */
	public final boolean shouldSample() {
		return sampleRate > 0.0 && sampleRate < 1.0;
	}
}
