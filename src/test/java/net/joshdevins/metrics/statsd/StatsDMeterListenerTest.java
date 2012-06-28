package net.joshdevins.metrics.statsd;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricName;

public class StatsDMeterListenerTest {

	private StatsDClient mockClient;

	private StatsDMeterListener listener;

	private Meter mockMeter;

	@Before
	public void before() {
		mockClient = mock(StatsDClient.class);
		listener = new StatsDMeterListener(mockClient);
		mockMeter = mock(Meter.class);
		when(mockMeter.getName()).thenReturn(metricName());
	}

	@Test
	public void mark() {
		listener.onMark(mockMeter, 1);
		verify(mockClient).count("group.type.name", 1);
	}

	@Test
	public void markWithSample() {
		listener.setSampleRate(0.1);
		listener.onMark(mockMeter, 1);
		verify(mockClient).count("group.type.name", 1, 0.1);
	}

	private MetricName metricName() {
		return new MetricName("group", "type", "name");
	}
}
