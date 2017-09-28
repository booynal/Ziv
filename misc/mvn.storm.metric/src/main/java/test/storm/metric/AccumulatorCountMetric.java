/**
 * AccumulatorCountMetric.java
 */
package test.storm.metric;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.storm.metric.api.IMetric;

/**
 * @author ziv
 * @date 2017年9月28日 上午9:59:14
 */
public class AccumulatorCountMetric implements IMetric {

	private AtomicLong counter = new AtomicLong(0);

	public long incr() {
		return counter.incrementAndGet();
	}

	@Override
	public Object getValueAndReset() {
		return counter.get();
	}

}
