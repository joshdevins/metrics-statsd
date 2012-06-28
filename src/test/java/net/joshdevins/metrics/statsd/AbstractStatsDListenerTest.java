package net.joshdevins.metrics.statsd;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.ObservableMetric;

public class AbstractStatsDListenerTest {

	private static class TestListener extends AbstractStatsDListener {

		protected TestListener(final StatsDClient client) {
			super(Counter.class, client);
		}

		void testFormatting(final ObservableMetric<?> metric) {
			super.extractBucketName(metric);
		}
	}

	private StatsDClient mockClient;

	private TestListener listener;

	@Before
	public void before() {
		mockClient = mock(StatsDClient.class);
		listener = new TestListener(mockClient);
	}

	@Test
	public void defaultMetricPredicate() {

		MetricPredicate metricPredicate = listener.getMetricPredicate();

		// name is ignored, we only care about the type of metric
		Assert.assertEquals(true,
				metricPredicate.matches(null, mock(Counter.class)));
		Assert.assertEquals(false,
				metricPredicate.matches(null, mock(Meter.class)));
	}

	@Test(expected = NullPointerException.class)
	public void failToSetBucketNameFormatter() {
		listener.setBucketNameFormatter(null);
	}

	@Test(expected = NullPointerException.class)
	public void failToSetMetricPredicate() {
		listener.setMetricPredicate(null);
	}

	@Test
	public void replaceDefaultBucketNameFormatter() {

		BucketNameFormatter mockBucketNameFormatter = mock(BucketNameFormatter.class);

		MetricName name = new MetricName("group", "type", "name");
		ObservableMetric<?> mockMetric = mock(ObservableMetric.class);
		when(mockMetric.getName()).thenReturn(name);

		listener.setBucketNameFormatter(mockBucketNameFormatter);
		listener.testFormatting(mockMetric);
		verify(mockBucketNameFormatter).format(name);

	}

	@Test
	public void replaceDefaultMetricPredicate() {

		MetricPredicate mockMetricPredicate = mock(MetricPredicate.class);
		listener.setMetricPredicate(mockMetricPredicate);
		Assert.assertEquals(mockMetricPredicate, listener.getMetricPredicate());
	}
}
