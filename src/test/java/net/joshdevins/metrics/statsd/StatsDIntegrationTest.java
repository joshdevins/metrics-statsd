package net.joshdevins.metrics.statsd;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricListenersRegistry;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;

/**
 * Remove {@link Ignore} annotations on individual tests.
 */
public class StatsDIntegrationTest {

    private static final Random RANDOM = new Random();

    private DefaultStatsDClient client;

    private MetricListenersRegistry listenersRegistry;

    @After
    public void after() {
        client.shutdown();
    }

    @Before
    public void before() throws IOException {
        client = new DefaultStatsDClient("statsd");
        listenersRegistry = Metrics.defaultListenersRegistry();
    }

    @Ignore
    @Test
    public void counterIntegration() throws InterruptedException {

        listenersRegistry.addListener(new StatsDCounterListener(client));
        final Counter counter = Metrics.newCounter(new MetricName(
                "metrics-statsd.test", "counter", "name"));

        // final outcome is random and independent of duration of the test
        doStuffForAWhile(1, new Runnable() {

            public void run() {

                if (RANDOM.nextBoolean()) {
                    counter.inc();
                } else {
                    counter.dec();
                }
            }
        });

        System.out.println("Last value: " + counter.getCount());
    }

    @Ignore
    @Test
    public void meterIntegration() throws InterruptedException {

        listenersRegistry.addListener(new StatsDMeterListener(client));
        final Meter meter = Metrics.newMeter(new MetricName(
                "metrics-statsd.test", "meter", "name"), "marks",
                TimeUnit.SECONDS);

        // current values should produce on around 20 marks/sec in Graphite
        int count = doStuffForAWhile(5, new Runnable() {

            public void run() {
                meter.mark();
            }
        });

        System.out.println("Total count:   " + meter.getCount());
        System.out.println("Mean rate:     " + meter.getMeanRate() + " "
                + meter.getEventType() + "/" + meter.getRateUnit());
        System.out.println("1-minute rate: " + meter.getOneMinuteRate() + " "
                + meter.getEventType() + "/" + meter.getRateUnit());
        System.out.println("5-minute rate: " + meter.getFiveMinuteRate() + " "
                + meter.getEventType() + "/" + meter.getRateUnit());

        Assert.assertEquals(count, meter.getCount());
    }

    @Ignore
    @Test
    public void timerIntegration() throws InterruptedException {

        listenersRegistry.addListener(new StatsDTimerListener(client));
        final Timer timer = Metrics.newTimer(new MetricName(
                "metrics-statsd.test", "timer", "name"), TimeUnit.MILLISECONDS,
                TimeUnit.SECONDS);

        int count = doStuffForAWhile(5, new Runnable() {

            public void run() {
                timer.update((long) (RANDOM.nextDouble() * 100),
                        TimeUnit.MILLISECONDS);
            }
        });

        System.out.println("Total count:    " + timer.getCount());
        System.out
                .println("Total duration: "
                        + timer.getSum()
                        + " ms = "
                        + TimeUnit.MILLISECONDS.toMinutes((long) timer.getSum())
                        + " m");
        System.out.println("Min duration:   " + timer.getMin() + " ms");
        System.out.println("Max duration:   " + timer.getMax() + " ms");
        System.out.println("Mean duration:  " + timer.getMean() + " ms");
        System.out.println("Std deviation:  " + timer.getStdDev() + " ms");
        System.out.println("Mean rate:      " + timer.getMeanRate() + " "
                + timer.getEventType());
        System.out.println("1-minute rate:  " + timer.getOneMinuteRate() + " "
                + timer.getEventType() + "/" + timer.getRateUnit());
        System.out.println("5-minute rate:  " + timer.getFiveMinuteRate() + " "
                + timer.getEventType() + "/" + timer.getRateUnit());
        System.out.println("15-minute rate: " + timer.getFifteenMinuteRate()
                + " " + timer.getEventType() + "/" + timer.getRateUnit());

        Assert.assertEquals(count, timer.getCount());
    }

    /**
     * Produce output to StatsD at sort of random intervals for a period of
     * time.
     */
    private int doStuffForAWhile(final int durationInMinutes,
            final Runnable runnable) throws InterruptedException {

        long durationOfTest = TimeUnit.MINUTES.toMillis(durationInMinutes);
        long start = new Date().getTime();
        int count = 0;

        while (start + durationOfTest > new Date().getTime()) {

            long sleepFor = (long) (RANDOM.nextDouble() * 100);
            Thread.sleep(sleepFor);

            count++;
            runnable.run();

            System.out.print(".");
            if (count % 100 == 0) {
                System.out.println();
            }
        }

        System.out.println();
        return count;
    }
}
