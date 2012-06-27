package net.joshdevins.metrics.realtime;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.Timer;

/**
 * A factory that creates realtime versions of all core {@link Metric}s and
 * registers them in a {@link MetricsRegistry}. These realtime metrics provide
 * hooks to send realtime notifications on metrics changes.
 * 
 * <p>
 * Note that if you change state on the underlying {@link Metric}, no realtime
 * listeners will be notified. Also note that listeners are called in sequence
 * so each listener needs to handle threading, executor pools, etc. on its' own.
 * </p>
 * 
 * <p>
 * All {@link Metric}s are supported except for {@link Gauge} which is
 * implemented in {@code metrics-core} as inherently pull-based and not
 * event-based.
 * </p>
 * 
 * @author Josh Devins
 */
public class RealtimeMetricsFactory {

	private final ConcurrentHashMap<Class<? extends Metric>, List<RealtimeMetricListener<? extends Metric>>> listeners;

	private final MetricsRegistry registry;

	public RealtimeMetricsFactory() {
		this(Metrics.defaultRegistry());
	}

	public RealtimeMetricsFactory(final MetricsRegistry registry) {

		listeners = new ConcurrentHashMap<Class<? extends Metric>, List<RealtimeMetricListener<? extends Metric>>>();
		this.registry = registry;
	}

	/**
	 * Adds a {@link RealtimeMetricListener} of the given {@link Metrics} type.
	 */
	public <T extends Metric, L extends RealtimeMetricListener<T>> boolean addListener(
			final L listener) {

		return getListenersFor(listener.getMetricType()).add(listener);
	}

	/**
	 * Creates a new {@link RealtimeCounter} and registers it under the given
	 * metric name.
	 * 
	 * @param metricName
	 *            the name of the metric
	 * @return a new {@link RealtimeCounter}
	 */
	public RealtimeCounter newCounter(final MetricName metricName) {

		Counter delegate = registry.newCounter(metricName);
		List<RealtimeCounterListener> listeners = getListenersFor(
				Counter.class, RealtimeCounterListener.class);

		return new RealtimeCounter(delegate, listeners);
	}

	/**
	 * Creates a new {@link Histogram} and registers it under the given metric
	 * name.
	 * 
	 * @param metricName
	 *            the name of the metric
	 * @param biased
	 *            whether or not the histogram should be biased
	 * @return a new {@link Histogram}
	 */
	public RealtimeHistogram newHistogram(final MetricName metricName,
			final boolean biased) {

		Histogram delegate = registry.newHistogram(metricName, biased);
		List<RealtimeHistogramListener> listeners = getListenersFor(Histogram.class);

		return new RealtimeHistogram(delegate, listeners);
	}

	/**
	 * Creates a new {@link Meter} and registers it under the given metric name.
	 * 
	 * @param metricName
	 *            the name of the metric
	 * @param eventType
	 *            the plural name of the type of events the meter is measuring
	 *            (e.g., {@code "requests"})
	 * @param unit
	 *            the rate unit of the new meter
	 * @return a new {@link Meter}
	 */
	public RealtimeMeter newMeter(final MetricName metricName,
			final String eventType, final TimeUnit unit) {

		Meter delegate = registry.newMeter(metricName, eventType, unit);
		List<RealtimeMeterListener> listeners = getListenersFor(Meter.class,
				RealtimeMeterListener.class);

		return new RealtimeMeter(delegate, listeners);
	}

	/**
	 * Creates a new {@link Timer} and registers it under the given metric name.
	 * 
	 * @param metricName
	 *            the name of the metric
	 * @param durationUnit
	 *            the duration scale unit of the new timer
	 * @param rateUnit
	 *            the rate scale unit of the new timer
	 * @return a new {@link Timer}
	 */
	public RealtimeTimer newTimer(final MetricName metricName,
			final TimeUnit durationUnit, final TimeUnit rateUnit) {

		Timer delegate = registry.newTimer(metricName, durationUnit, rateUnit);
		List<RealtimeTimerListener> listeners = getListenersFor(Timer.class,
				RealtimeTimerListener.class);

		return new RealtimeTimer(delegate, listeners);
	}

	/**
	 * Removes a {@link RealtimeMetricListener} of the given {@link Metrics}
	 * type.
	 */
	public <T extends Metric> boolean removeListener(
			final RealtimeMetricListener<? extends Metric> listener) {

		List<RealtimeMetricListener<? extends Metric>> list = listeners
				.get(listener.getMetricType());

		if (list != null) {
			return list.remove(listener);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Metric, L extends RealtimeMetricListener<T>> List<L> getListenersFor(
			final Class<T> klass) {

		return (List<L>) listeners
				.putIfAbsent(
						klass,
						(List<RealtimeMetricListener<? extends Metric>>) new CopyOnWriteArrayList<L>());
	}

	@SuppressWarnings("unchecked")
	private <T extends Metric, L> List<L> getListenersFor(final Class<T> klass,
			final Class<L> listenerKlass) {

		return (List<L>) getListenersFor(klass);
	}
}
