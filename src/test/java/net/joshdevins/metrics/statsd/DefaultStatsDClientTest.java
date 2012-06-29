package net.joshdevins.metrics.statsd;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class DefaultStatsDClientTest {

    private static final int SAMPLE_SIZE = 10000;

    private StatsDConnection mockConnection;

    private DefaultStatsDClient client;

    @Before
    public void before() {
        mockConnection = mock(StatsDConnection.class);
        client = new DefaultStatsDClient(mockConnection);
    }

    @Test
    public void creatingClient() {
        verify(mockConnection).connect();
    }

    @Test
    public void sendCounterValue() {
        client.count("bucket", 1);
        verify(mockConnection).send("bucket:1|c");
    }

    @Test
    public void sendCounterValueSampled() {

        // for a sufficiently large sample set, verify within x% accuracy
        for (int i = 0; i < 10000; i++) {
            client.count("bucket", 1, 0.1);
        }

        verifySampleVariance("bucket:1|c|@0.1", 0.1);
    }

    @Test
    public void sendGaugeValue() {
        client.gauge("bucket", 100);
        verify(mockConnection).send("bucket:100|g");
    }

    @Test
    public void sendTimingValue() {
        client.timing("bucket", 100);
        verify(mockConnection).send("bucket:100|ms");
    }

    @Test
    public void sendTimingValueSampled() {

        // for a sufficiently large sample set, verify within x% accuracy
        for (int i = 0; i < SAMPLE_SIZE; i++) {
            client.timing("bucket", 100, 0.1);
        }

        verifySampleVariance("bucket:100|ms|@0.1", 0.1);
    }

    @Test
    public void shutdownClient() {

        client.shutdown();
        verify(mockConnection).close();
    }

    private void verifySampleVariance(final String message,
            final double samplingRate) {

        double samplingVariancePct = 0.1;
        int samplingVariance = new Double(SAMPLE_SIZE * samplingRate
                * samplingVariancePct).intValue();
        int perfectSampling = new Double(SAMPLE_SIZE * samplingRate).intValue();
        int min = perfectSampling - samplingVariance;
        int max = perfectSampling + samplingVariance;

        verify(mockConnection, atLeast(min)).send(message);
        verify(mockConnection, atMost(max)).send(message);
    }
}
