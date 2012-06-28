package net.joshdevins.metrics.statsd;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatsDUdpConnectionTest {

	private static class UdpServer {

		final int port;

		BlockingQueue<String> messages;

		private boolean shutdown = false;

		private final DatagramSocket serverSocket;

		private final byte[] receiveData;

		private final Thread thread;

		UdpServer() throws IOException {
			port = getRandomUnusedPort();
			serverSocket = new DatagramSocket(port);
			receiveData = new byte[1024];
			messages = new ArrayBlockingQueue<String>(1);

			thread = new Thread() {
				@Override
				public void run() {
					while (!shutdown) {
						DatagramPacket receivePacket = new DatagramPacket(
								receiveData, receiveData.length);
						try {
							serverSocket.receive(receivePacket);
						} catch (IOException e) {
							shutdown = true;
						}

						byte[] message = new byte[receivePacket.getLength()];
						System.arraycopy(receivePacket.getData(),
								receivePacket.getOffset(), message, 0,
								receivePacket.getLength());
						messages.add(new String(message));
					}
				}
			};
		}

		void start() {
			thread.start();
		}

		void stop() {
			shutdown = true;
			serverSocket.close();
		}
	}

	private UdpServer server;

	private StatsDUdpConnection connection;

	@After
	public void after() {
		server.stop();
		connection.close();
	}

	@Before
	public void before() throws IOException {
		server = new UdpServer();
		server.start();
		connection = new StatsDUdpConnection("127.0.0.1", server.port);
	}

	@Test
	public void sendMessage() throws InterruptedException {
		connection.send("message");
		Assert.assertEquals("message", server.messages.take());
	}

	/**
	 * Provides a quick way to get a random, unused port by opening a
	 * {@link ServerSocket} and getting the locally assigned port for the server
	 * socket.
	 * 
	 * @return A random, unused port.
	 * 
	 * @throws IllegalStateException
	 *             Thrown if any {@link IOException}s are thrown by the
	 *             underlying calls to {@link ServerSocket}.
	 */
	public static int getRandomUnusedPort() {

		final int port;
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			port = socket.getLocalPort();
		} catch (final Exception e) {
			throw new IllegalStateException(
					"Failure picking a random, unused port.", e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (final IOException ioe) {
					throw new IllegalStateException(
							"Failure closing temporary socket.", ioe);
				}
			}
		}

		return port;
	}
}
