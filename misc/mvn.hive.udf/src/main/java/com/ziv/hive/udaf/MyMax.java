/**
 * MyMax.java
 */
package com.ziv.hive.udaf;

import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;
import org.apache.hadoop.io.IntWritable;

/**
 * @author ziv
 * @date 2017年12月11日 上午12:18:49
 */
public class MyMax extends UDAF {

	public static class MyMaxIntUdafEvaluator implements UDAFEvaluator {

		private IntWritable result;

		@Override
		public void init() {
			result = null;
		}

		public boolean iterate(IntWritable value) {
			if (null == value) {
				return true;
			}

			if (null == result) {
				result = new IntWritable(value.get());
			} else {
				result.set(Math.max(result.get(), value.get()));
			}
			return true;
		}

		public IntWritable terminatePartial() {
			return result;
		}

		public boolean merge(IntWritable other) {
			return iterate(other);
		}

		public IntWritable terminate() {
			return result;
		}
	}

}
