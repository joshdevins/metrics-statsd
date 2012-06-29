package net.joshdevins.metrics.statsd;

import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.Timer;

public class StatsDTimerListenerTest {

    private StatsDClient mockClient;

    private StatsDTimerListener listener;

    private Timer mockTimer;

    @Before
    public void before() {
        mockClient = mock(StatsDClient.class);
        listener = new StatsDTimerListener(mockClient);
        mockTimer = mock(Timer.class);
        when(mockTimer.getName()).thenReturn(metricName());
    }

    @Test
    public void mark() {
        listener.onUpdate(mockTimer, 1, TimeUnit.SECONDS);
        verify(mockClient).timing("group.type.name", 1000);
    }

    @Test
    public void markWithSample() {
        listener.setSampleRate(0.1);
        listener.onUpdate(mockTimer, 1, TimeUnit.SECONDS);
        verify(mockClient).timing("group.type.name", 1000, 0.1);
    }

    private MetricName metricName() {
        return new MetricName("group", "type", "name");
    }
}
