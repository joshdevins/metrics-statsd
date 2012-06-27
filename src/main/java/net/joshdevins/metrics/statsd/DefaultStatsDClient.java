package net.joshdevins.metrics.statsd;

import java.io.IOException;
import java.util.Random;

/**
 * A simple {@link StatsDClient} providing a few guarantees to the caller of the
 * client. Most notably, the client is always non-blocking and will never pass
 * exceptions back up the stack to the caller. The only exception to this is on
 * creation of this object which can fail.
 * 
 * @author Josh Devins
 * 
 * @see <a
 *      href="https://github.com/youdevise/java-statsd-client/blob/master/src/main/java/com/timgroup/statsd/StatsDClient.java">YouDevise
 *      StatsDClient</a>
 * @see <a
 *      href="https://github.com/etsy/statsd/blob/master/examples/StatsdClient.java">StatsD
 *      Example Java client</a>
 */
public class DefaultStatsDClient implements StatsDClient {

	/**
	 * Random number generator with a psuedo-random seed based on local clock.
	 */
	private final Random RANDOM = new Random();

	private final StatsDConnection connection;

	/**
	 * Default constructor allowing the {@link StatsDConnection} to be
	 * explicitly set.
	 * 
	 * @param connection
	 *            the underlying {@link StatsDConnection} to use
	 */
	public DefaultStatsDClient(final StatsDConnection connection) {
		this.connection = connection;
	}

	/**
	 * Creates a new client using the default {@link StatsDUdpConnection}.
	 * 
	 * @throws IOException
	 *             if a UDP socket could not be established
	 * 
	 * @see StatsDUdpConnection#StatsDUdpConnection(String, int)
	 */
	public DefaultStatsDClient(final String host, final int port)
			throws IOException {

		this(new StatsDUdpConnection(host, port));
	}

	public boolean count(final String bucket, final long delta) {
		return connection.send(String.format("%s:%d|c", bucket, delta));
	}

	public boolean count(final String bucket, final long delta,
			final double sampleRate) {

		// see if we should sample this call
		if (RANDOM.nextDouble() <= sampleRate) {

			String message = String.format("%s:%d|c|@%f", bucket, delta,
					sampleRate);
			return connection.send(message);
		}

		// this was successful since it was not sampled
		return true;
	}

	public boolean gauge(final String bucket, final long value) {
		return connection.send(String.format("%s:%d|g", bucket, value));
	}

	public boolean timing(final String bucket, final long timeInMillis) {
		return connection.send(String.format("%s:%d|ms", bucket, timeInMillis));
	}

	public boolean timing(final String bucket, final long timeInMillis,
			final double sampleRate) {

		// see if we should sample this call
		if (RANDOM.nextDouble() <= sampleRate) {

			String message = String.format("%s:%d|ms|@%f", bucket,
					timeInMillis, sampleRate);
			return connection.send(message);
		}

		// this was successful since it was not sampled
		return true;
	}
}
