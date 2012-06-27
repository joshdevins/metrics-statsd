package net.joshdevins.metrics.realtime.statsd;

import net.joshdevins.metrics.realtime.RealtimeCounter;
import net.joshdevins.metrics.realtime.RealtimeCounterListener;

public class StatsDCounterListener extends RealtimeCounterListener {

	private final StatsDClient client;

	public StatsDCounterListener(final StatsDClient client) {
		this.client = client;
	}

	@Override
	public void onChange(final RealtimeCounter counter, final long delta) {
		client.gauge("", delta);
	}

	@Override
	public void onClear(final RealtimeCounter counter) {
		client.gauge("", 0);
	}

}
