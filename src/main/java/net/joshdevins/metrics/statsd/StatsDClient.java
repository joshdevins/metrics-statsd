package net.joshdevins.metrics.statsd;

/**
 * The contract defining how we can interact with the StatsD backend. This is in
 * line with the StatsD backend as of version 0.3.0.
 * 
 * @author Josh Devins
 * 
 * @see <a
 *      href="https://github.com/etsy/statsd/blob/11049a2ee78ae1bcb22460388ca3d224a26d8213">StatsD
 *      v0.3.0</a>
 */
public interface StatsDClient {

	/**
	 * For the given {@code bucket}, change the counter by the given
	 * {@code delta} value.
	 * 
	 * @return success/failure of the operation
	 */
	public boolean count(String bucket, long delta);

	/**
	 * For the given {@code bucket}, change the counter by the given
	 * {@code delta} value.
	 * 
	 * @param sampleRate
	 *            sample the calls at the provided percentage rate, for example
	 *            a rate of 0.1 will send only 10% of calls to the backend
	 * 
	 * @return success/failure of the operation
	 */
	public boolean count(String bucket, long delta, double sampleRate);

	/**
	 * For the given {@code bucket}, set the gauge to the given {@code value}.
	 * 
	 * @return success/failure of the operation
	 */
	public boolean gauge(String bucket, long value);

	/**
	 * For the given {@code bucket}, send a timer value of {@code timeInMillis}.
	 * 
	 * @return success/failure of the operation
	 */
	public boolean timing(String bucket, long timeInMillis);

	/**
	 * For the given {@code bucket}, send a timer value of {@code timeInMillis}.
	 * 
	 * @param sampleRate
	 *            sample the calls at the provided percentage rate, for example
	 *            a rate of 0.1 will send only 10% of calls to the backend
	 * 
	 * @return success/failure of the operation
	 */
	public boolean timing(String bucket, long timeInMillis, double sampleRate);
}
