/**
 * Strip.java
 */
package com.ziv.hive.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @author ziv
 * @date 2017年12月10日 下午11:15:24
 */
public class Strip extends UDF {

	private Text result = new Text();

	public Text evaluate(Text str) {
		if (null == str) {
			return null;
		} else {
			result.set(StringUtils.strip(str.toString()) + "; " + "ziv");
			return result;
		}
	}

	public Text evaluate(Text str, String stripChars) {
		if (null == str) {
			return null;
		} else {
			result.set(StringUtils.strip(str.toString(), stripChars) + "; " + "ziv");
			return result;
		}
	}
}
