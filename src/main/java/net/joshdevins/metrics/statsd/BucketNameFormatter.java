package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.MetricName;

/**
 * Interface for formatting bucket names to send to StatsD.
 * 
 * @author Josh Devins
 */
public interface BucketNameFormatter {

	/**
	 * Given a {@link MetricName}, format a bucket name for StatsD.
	 * 
	 * @param name
	 *            the {@link MetricName} to convert
	 * 
	 * @return the formatted bucket name
	 */
	String format(MetricName name);
}
