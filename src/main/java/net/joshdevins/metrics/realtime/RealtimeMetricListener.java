package net.joshdevins.metrics.realtime;

import com.yammer.metrics.core.Metric;

public interface RealtimeMetricListener<T extends Metric> {

	public Class<T> getMetricType();

}
