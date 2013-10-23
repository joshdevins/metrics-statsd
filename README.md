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

// for convenience, if you just want to use all the defaults, use the StatsD helper class
// this sets up all available listeners and returns a client (so you can shut it down later)
client = StatsD.setupWithDefaults("localhost")

```

# TODO

Add a Metrics reporter that can push non-realtime stats from metrics into StatsD. See other metrics-statsd projects as well.

# Installation

Since this currently relies on the fork [joshdevins/metrics](https://github.com/joshdevins/metrics), the prerequisite is that you head over there, clone the repo and `mvn install` to build the forked `metrics-core`.

Once you have `metrics-core` installed locally, you can simply clone this repo and do a `mvn install`.

# License

The MIT License (MIT)

Copyright (c) 2013 SoundCloud, Josh Devins

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
