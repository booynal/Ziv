package test.sort.util;

/**
 * 项目名称:structure
 * 包名称:  test.sort.quick
 * 类描述:  排序辅助类
 * 创建人:  ziv
 * 创建时间: 2018/4/9 10:24
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class SorterUtil {

	/**
	 * 方法描述: 判断一个数组是否已经排序
	 *
	 * @param array 待判断的数组
	 * @return 如果已经排序，则返回true，否则返回false
	 * @author ziv
	 * @date 2018/4/9 10:13
	 */
	public static boolean isOrdering(int[] array) {
		if (null == array) {
			return false;
		}

		if (array.length < 3) {
			return true;
		}

		final boolean isOrdering = isGreatThanZero(array, 0);
		for (int i = 1; i < array.length - 1; i++) {
			if (isGreatThanZero(array, i) != isOrdering) {
				return false;
			}
		}
		return true;
	}

	private static boolean isGreatThanZero(int[] array, int i) {
		return array[i] - array[i + 1] > 0;
	}
}
