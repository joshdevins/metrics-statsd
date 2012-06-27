package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.CounterListener;

public class StatsDCounterListener extends AbstractStatsDListener implements
		CounterListener {

	public StatsDCounterListener(final StatsDClient client) {
		super(Counter.class, client);
	}

	public void onClear(final Counter counter) {
		getClient().gauge(counter.getName().getMBeanName(), 0);
	}

	public void onUpdate(final Counter counter, final long delta) {
		getClient().gauge(counter.getName().getMBeanName(), delta);
	}
}
