package test.sort.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 项目名称:structure
 * 包名称:  test.sort.util
 * 类描述:  排序辅助测试类
 * 创建人:  ziv
 * 创建时间: 2018/4/9 10:27
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class SorterUtilTest {

	@Test
	public void isOrdering() {
		boolean ordering;
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{1}));
		Assert.assertTrue(ordering);
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{1, 2}));
		Assert.assertTrue(ordering);
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{2, 1}));
		Assert.assertTrue(ordering);
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{1, 2, 3}));
		Assert.assertTrue(ordering);
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{3, 2, 1}));
		Assert.assertTrue(ordering);
		System.out.println(ordering = SorterUtil.isOrdering(new int[]{3, 4, 1}));
		Assert.assertFalse(ordering);
	}

}
