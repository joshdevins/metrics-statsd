package net.joshdevins.metrics.statsd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.yammer.metrics.core.MetricName;

public class DefaultBucketNameFormatterTest {

    private DefaultBucketNameFormatter formatter;

    @Before
    public void before() {
        formatter = new DefaultBucketNameFormatter();
    }

    @Test
    public void formatWithAllProperties() {
        Assert.assertEquals("group.type.[scope].name", formatter
                .format(new MetricName("group", "type", "name", "scope")));
    }

    @Test
    public void formatWithMinimumProperties() {
        Assert.assertEquals("group.type.name",
                formatter.format(new MetricName("group", "type", "name")));
    }
}
