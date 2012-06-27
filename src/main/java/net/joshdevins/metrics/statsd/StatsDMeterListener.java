package net.joshdevins.metrics.statsd;

import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MeterListener;

public class StatsDMeterListener extends AbstractSamplingStatsDListener
		implements MeterListener {

	protected StatsDMeterListener(final StatsDClient client) {
		super(Meter.class, client);
	}

	public void onMark(final Meter meter, final long n) {

		if (shouldSample()) {
			getClient().count(meter.getName().getMBeanName(), n,
					getSampleRate());
		} else {
			getClient().count(meter.getName().getMBeanName(), n);
		}
	}
}
