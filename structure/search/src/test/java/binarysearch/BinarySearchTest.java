package binarysearch;

import org.junit.Assert;
import org.junit.Test;

/**
 * 项目名称:search
 * 包名称:  binarysearch
 * 类描述:  对二分查找算法进行实现和测试
 * 创建人:  ziv
 * 创建时间: 2018/3/25 01:10
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class BinarySearchTest {

	@Test
	public void test() {
		System.out.println("对二分查找算法进行实现和测试");
		int[] arr = new int[]{1, 3, 4, 6, 9, 11, 22, 35, 39, 65, 100, 239};
		int index;
		for (int a : arr) {
			System.out.println("元素下标为: " + (index = binarySearch(arr, a)));
			Assert.assertTrue(index >= 0);
		}

		System.out.println("测试单个元素的数组: " + (index = binarySearch(new int[]{1}, 1)));
		Assert.assertEquals(0, index);
		System.out.println("测试单个元素的数组: " + (index = binarySearch(new int[]{2}, 1)));
		Assert.assertEquals(-1, index);
	}

	/**
	 * 方法描述: 二分查找算法，要求数组是已经生序排序的
	 *
	 * @param array  待查找的数组
	 * @param target 查找的元素
	 * @return 找到的元素下标，如果没找到则返回-1
	 * @author ziv
	 * @date 2018/3/25 01:14
	 */
	public int binarySearch(int[] array, int target) {
		if (null == array || array.length == 0) {
			return -1;
		}

		return binarySearch(array, 0, array.length - 1, target);
	}

	private int binarySearch(int[] array, int min, int max, int target) {
		if (max - min <= 1) {
			return array[min] == target ? min : array[max] == target ? max : -1;
		}
		int mid = (max + min) / 2;
		final int midv = array[mid];
		if (target < midv) {
			return binarySearch(array, min, mid, target);
		} else if (target > midv) {
			return binarySearch(array, mid, max, target);
		} else {
			return mid;
		}
	}
}
