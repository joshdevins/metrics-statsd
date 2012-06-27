package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MeterListener;

public class StatsDMeterListener extends AbstractStatsDListener implements
		MeterListener {

	protected StatsDMeterListener(final StatsDClient client) {
		super(Meter.class, client);
	}

	public void onMark(final Meter meter, final long n) {
		getClient().count(meter.getName().getMBeanName(), n);
	}
}
