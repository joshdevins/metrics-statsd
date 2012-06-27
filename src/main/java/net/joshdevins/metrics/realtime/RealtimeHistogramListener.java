package net.joshdevins.metrics.realtime;

import com.yammer.metrics.core.Histogram;

public abstract class RealtimeHistogramListener implements
		RealtimeMetricListener<Histogram> {

	public Class<Histogram> getMetricType() {
		return Histogram.class;
	}

	public abstract void onClear(RealtimeHistogram histogram);

	public abstract void onUpdate(RealtimeHistogram histogram, long value);
}
