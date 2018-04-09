package test.sort.quick;

import org.junit.Assert;
import org.junit.Test;
import test.sort.util.SorterUtil;

import java.util.Arrays;

/**
 * 项目名称:structure
 * 包名称:  test.sort.quick
 * 类描述:  快速排序测试类
 * 创建人:  ziv
 * 创建时间: 2018/4/9 09:28
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class QuickSorterTest {

	@Test
	public void quickSort() {
		int[] array = new int[]{5, 3, 6, 3, 6, 5, 9, 1, 8, 2, 4};
		Assert.assertFalse(SorterUtil.isOrdering(array));
		System.out.println(Arrays.toString(array));
		QuickSorter.debug = false;
		int[] ints = QuickSorter.quickSort(array);
		Assert.assertTrue(SorterUtil.isOrdering(ints));
		System.out.println(Arrays.toString(ints));
	}
}
