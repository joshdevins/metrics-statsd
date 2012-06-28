# Introduction

An extension to [joshdevins/metrics](https://github.com/joshdevins/metrics) (fork of [codahale/metrics](https://github.com/codahale/metrics)) providing the ability to send realtime metrics to StatsD.

# Usage

```java
// create a StatsD client with the host:port of your choosing
client = new DefaultStatsDClient("localhost", "9387");

// use Metric to access the default MetricListenersRegistry
listenersRegistry = Metric.getDefaultMetricListenersRegistry();

// create listeners for the things you care about
// by default, all the listeners will listen to all Metrics of their class
counterListener = new StatsDCounterListener(client);
listenersRegistry.addListener(counterListener);

// override some default functionality
specialCounterListener = new StatsDCounterListener(client);

// override the MetricPredicate for fine-grained control of which metrics the listener should be called for, but do so BEFORE registering the listener with the registry
specialCounterListener.setMetricPredicate(metricPredicate);

// also override the BucketNameFormatter to send different bucket names to StatsD
// the default bucket names are: group.type.[scope].name, where scope is inserted only if provided in your metric's name
specialCounterListener.setBucketNameFormatter(formatter);

listenersRegistry.addListener(specialCounterListener);

```

# Installation

Since this currently relies on the fork [joshdevins/metrics](https://github.com/joshdevins/metrics), the prerequisite is that you head over there, clone the repo and `mvn install` to build the forked `metrics-core`.

Once you have `metrics-core` installed locally, you can simply clone this repo and do a `mvn install`.
