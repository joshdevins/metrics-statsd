# Introduction

An extension to [joshdevins/metrics](https://github.com/joshdevins/metrics) (fork of [codahale/metrics](https://github.com/codahale/metrics)) providing the ability to send realtime metrics to StatsD.

# Usage

```java
// create a StatsD client with the host:port of your choosing (or port 8125 by default)
client = new DefaultStatsDClient("localhost");

// use Metric to access the default MetricListenersRegistry
listenersRegistry = Metrics.defaultListenersRegistry();

// create listeners for the things you care about
// by default, all the listeners will listen to all Metrics of their class
listenersRegistry.addListener(new StatsDCounterListener(client));

// override some default functionality
specialCounterListener = new StatsDCounterListener(client);

// override the MetricPredicate for fine-grained control of which metrics the listener
//  should be called for, but do so BEFORE registering the listener with the registry
metricPredicate = new MetricPredicate() {
    public boolean matches(final MetricName name, final Metric metric) {
        // I only care about "foo.bar.*" metrics
        return name.getGroup().startsWith("foo.bar.*");
    }
};
specialCounterListener.setMetricPredicate(metricPredicate);

// also override the BucketNameFormatter to send different bucket names to StatsD
// the default bucket names are: group.type.[scope].name
specialCounterListener.setBucketNameFormatter(formatter);

listenersRegistry.addListener(specialCounterListener);

```

# TODO

Add a Metrics reporter that can push non-realtime stats from metrics into Graphite directly or through StatsD.

# Installation

Since this currently relies on the fork [joshdevins/metrics](https://github.com/joshdevins/metrics), the prerequisite is that you head over there, clone the repo and `mvn install` to build the forked `metrics-core`.

Once you have `metrics-core` installed locally, you can simply clone this repo and do a `mvn install`.
