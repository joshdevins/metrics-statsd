package net.joshdevins.metrics.realtime;

import java.util.concurrent.TimeUnit;

import com.yammer.metrics.core.Timer;

public abstract class RealtimeTimerListener implements
		RealtimeMetricListener<Timer> {

	public Class<Timer> getMetricType() {
		return Timer.class;
	}

	public abstract void onUpdate(RealtimeTimer timer, long duration,
			TimeUnit unit);
}
