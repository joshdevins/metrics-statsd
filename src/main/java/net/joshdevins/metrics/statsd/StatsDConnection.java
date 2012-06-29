package net.joshdevins.metrics.statsd;

/**
 * An interface describing a connection to StatsD.
 * 
 * @author Josh Devins
 * 
 */
public interface StatsDConnection {

    /**
     * Closes the underlying StatsD connection.
     */
    public void close();

    /**
     * Establishes the underlying StatsD connection.
     * 
     * @return success/failure of the connection setup
     */
    public boolean connect();

    /**
     * Sends the {@link String}-based message to the StatsD backend.
     * 
     * @param message
     *        the message to send
     * @return success/failure of delivery of all bytes of the message
     */
    public boolean send(String message);
}
