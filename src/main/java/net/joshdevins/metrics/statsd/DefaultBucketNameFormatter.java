package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.MetricName;

/**
 * A {@link BucketNameFormatter} that provides the following format from a
 * {@link MetricName}: <code>group.type.[scope].name</code> where scope and name
 * parts are not inserted unless they are specified.
 * 
 * @author Josh Devins
 */
public class DefaultBucketNameFormatter implements BucketNameFormatter {

	public String format(final MetricName name) {

		StringBuilder sb = new StringBuilder();
		sb.append(name.getGroup().trim());
		sb.append(".");
		sb.append(name.getType().trim());

		// scope gets some square brackeys after type to be clear it's not name
		if (name.getScope() != null && name.getScope().trim().length() > 0) {
			sb.append(".[");
			sb.append(name.getScope().trim());
			sb.append("]");
		}

		if (name.getName() != null && name.getName().trim().length() > 0) {
			sb.append(".");
			sb.append(name.getName().trim());
		}

		return sb.toString();
	}
}
