package net.joshdevins.metrics.realtime;

import com.yammer.metrics.core.Meter;

public abstract class RealtimeMeterListener implements
		RealtimeMetricListener<Meter> {

	public Class<Meter> getMetricType() {
		return Meter.class;
	}

	public abstract void onMark(RealtimeMeter meter, long n);
}
