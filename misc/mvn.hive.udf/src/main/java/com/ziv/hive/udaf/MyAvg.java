/**
 * MyAvg.java
 */
package com.ziv.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.DoubleWritable;

/**
 * 求平均数 TODO 未通过验证
 * 
 * @author ziv
 * @date 2017年12月11日 上午12:57:52
 */
public class MyAvg extends UDAF {

	public static class MyAvgIntUdafEvaluator implements UDAFEvaluator {

		public static class PartialResult {

			double sum;
			long count;
		}

		private PartialResult partial;

		@Override
		public void init() {
			partial = null;
		}

		public boolean iterate(DoubleWritable value) {
			if (null == value) {
				return true;
			}

			if (null == partial) {
				partial = new PartialResult();
			}
			partial.sum += value.get();
			partial.count++;
			return true;
		}

		public PartialResult terminatePartial() {
			return partial;
		}

		public boolean merge(PartialResult other) {
			if (null == other) {
				return true;
			}

			if (null == partial) {
				partial = new PartialResult();
			}
			partial.sum += other.sum;
			partial.count += other.count;
			return true;
		}

		public DoubleWritable terminate() {
			if (null == partial) {
				return null;
			} else {
				return new DoubleWritable(partial.sum / partial.count);
			}
		}

	}
}
