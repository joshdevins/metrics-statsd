package net.joshdevins.metrics.realtime;

import com.yammer.metrics.core.Counter;

public abstract class RealtimeCounterListener implements
		RealtimeMetricListener<Counter> {

	public Class<Counter> getMetricType() {
		return Counter.class;
	}

	public abstract void onChange(RealtimeCounter counter, long delta);

	public abstract void onClear(RealtimeCounter counter);
}
