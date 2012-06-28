package net.joshdevins.metrics.statsd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A concrete {@link StatsDConnection} using a {@link DatagramChannel} as the
 * underlying communication mechanism. At runtime, this will be the default
 * {@link StatsDConnection} implementation that will be used.
 * 
 * @author Josh Devins
 */
public class StatsDUdpConnection implements StatsDConnection {

	/**
	 * The default StatsD port: 8125
	 */
	public static final int DEFAULT_PORT = 8125;

	/**
	 * The default message encoding: UTF-8
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	private static Logger LOG = LoggerFactory
			.getLogger(StatsDUdpConnection.class);

	private final InetSocketAddress address;

	private final DatagramChannel channel;

	/**
	 * Creates an underlying non-blocking {@link DatagramChannel} to use for UDP
	 * communication. Uses port <code>8125</code> by default.
	 * 
	 * @see StatsDUdpConnection#StatsDUdpConnection(String, int)
	 */
	public StatsDUdpConnection(final String host) throws IOException {
		this(host, DEFAULT_PORT);
	}

	/**
	 * Creates an underlying non-blocking {@link DatagramChannel} to use for UDP
	 * communication.
	 * 
	 * @param host
	 *            host to connect to
	 * @param port
	 *            port to connect to
	 * 
	 * @throws IOException
	 *             on failure to open underlying {@link DatagramChannel}
	 */
	public StatsDUdpConnection(final String host, final int port)
			throws IOException {

		address = new InetSocketAddress(host, port);
		channel = DatagramChannel.open();
		channel.configureBlocking(false);
	}

	public void close() {
		try {
			channel.close();
		} catch (IOException ioe) {
			LOG.warn("Failed to cleanly close the underlying DatagramChannel",
					ioe);
		}
	}

	public boolean connect() {
		try {
			channel.connect(address);
		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * Sends the message over the UDP channel using UTF-8 to decode into bytes.
	 * This uses an underlying non-blocking {@link DatagramChannel} so it should
	 * never block the caller.
	 */
	public boolean send(final String message) {

		// extract bytes from UTF-8 string
		byte[] bytes = new byte[0];
		try {
			bytes = message.getBytes(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			LOG.error(
					"Failed to convert message from String to bytes using the default encoding {}. message=\"{}\"",
					DEFAULT_ENCODING, message);
			return false;
		}

		ByteBuffer src = ByteBuffer.wrap(bytes);

		// quick check to ensure socket is still connected to destination UDP
		if (!channel.isConnected()) {
			connect();
		}

		// send bytes over UDP
		int sentBytes = 0;
		try {
			sentBytes = channel.write(src);
		} catch (Throwable t) {
			LOG.error("Failed to send message. message=\"" + message + "\"", t);
			return false;
		}

		// ensure all bytes were actually sent
		if (bytes.length != sentBytes) {
			LOG.error(
					"Failed to send entirety of message. Only sent {} out of {} bytes. message=\"{}\"",
					new Object[] { Integer.valueOf(sentBytes),
							Integer.valueOf(bytes.length), message });
			return false;
		}

		return true;
	}
}
