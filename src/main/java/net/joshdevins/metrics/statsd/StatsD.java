package net.joshdevins.metrics.statsd;

import java.io.IOException;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricsRegistry;

/**
 * Static helper class containing setup methods typically using defaults.
 * 
 * @author Josh Devins
 */
public final class StatsD {

    private StatsD() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a StatsD listener for all of the available metrics and registers
     * it with the default {@link MetricsRegistry}.
     * 
     * @param client
     *        the {@link StatsDClient} for the new listeners
     */
    public static void setupWithDefaults(final StatsDClient client) {

        Metrics.defaultListenersRegistry().addListener(
                new StatsDCounterListener(client));
        Metrics.defaultListenersRegistry().addListener(
                new StatsDMeterListener(client));
        Metrics.defaultListenersRegistry().addListener(
                new StatsDTimerListener(client));
    }

    /**
     * Creates a StatsD listener for all of the available metrics and registers
     * it with the default {@link MetricsRegistry}.
     * 
     * @param host
     *        the StatsD host to connect to
     * 
     * @return the {@link StatsDClient} that was created
     * 
     * @throws IOException
     *         on failure to setup a {@link StatsDClient}
     */
    public static StatsDClient setupWithDefaults(final String host)
            throws IOException {

        StatsDClient client = new DefaultStatsDClient(host);
        setupWithDefaults(client);

        return client;
    }
}
