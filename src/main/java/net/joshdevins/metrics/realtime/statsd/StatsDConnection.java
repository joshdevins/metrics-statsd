package net.joshdevins.metrics.realtime.statsd;

/**
 * An interface describing a connection to StatsD.
 * 
 * @author Josh Devins
 * 
 */
public interface StatsDConnection {

	/**
	 * Sends the {@link String}-based message to the StatsD backend.
	 * 
	 * @param message
	 *            the message to send
	 * @return success/failure of delivery of all bytes of the message
	 */
	public boolean send(String message);

	/**
	 * Sets up the underlying StatsD connection.
	 * 
	 * @return success/failure of the connection setup
	 */
	public boolean setup();

	/**
	 * Tears down the underlying StatsD connection.
	 */
	public void tearDown();
}
