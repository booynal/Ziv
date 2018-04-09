package test.sort.quick;

import java.util.Arrays;

/**
 * 项目名称:structure
 * 包名称:  test.sort.quick
 * 类描述:  快速排序器
 * 创建人:  ziv
 * 创建时间: 2018/4/9 09:14
 * 修改人:
 * 修改时间:
 * 修改备注:
 */
public class QuickSorter {

	static boolean debug = true;

	// 排序一串数字
	public static int[] quickSort(int[] array) {
		if (null == array || array.length < 2) {
			return array;
		}

		int seeder = array[0];
		int[] leftArray = createNewTempArray(array.length);
		int[] middleArray = createNewTempArray(array.length);
		int[] rightArray = createNewTempArray(array.length);
		int leftIndex = 0, middleIndex = 0, rightIndex = 0;
		middleArray[middleIndex++] = seeder;
		for (int i = 1; i < array.length; i++) {
			int v = array[i];
			if (v > seeder) {
				rightArray = putValue(rightArray, rightIndex++, v);
			} else if (v < seeder) {
				leftArray = putValue(leftArray, leftIndex++, v);
			} else {
				middleArray = putValue(middleArray, middleIndex++, v);
			}
		}

		int[] leftArrayTmp = new int[leftIndex];
		System.arraycopy(leftArray, 0, leftArrayTmp, 0, leftArrayTmp.length);
		int[] leftArraySub = quickSort(leftArrayTmp);
		int[] rightArrayTmp = new int[rightIndex];
		System.arraycopy(rightArray, 0, rightArrayTmp, 0, rightArrayTmp.length);
		int[] rightArraySub = quickSort(rightArrayTmp);

		int[] resultArray = new int[array.length];
		System.arraycopy(leftArraySub, 0, resultArray, 0, leftIndex);
		System.arraycopy(middleArray, 0, resultArray, leftIndex, middleIndex);
		System.arraycopy(rightArraySub, 0, resultArray, leftIndex + middleIndex, rightIndex);

		if (debug) {
			System.out.println(String.format("input array: (%s)%s", array.length, Arrays.toString(array)));
			System.out.println("seeder: " + seeder);
			System.out.println(String.format("leftArray: '(%s)%s'", leftIndex, Arrays.toString(leftArray)));
			System.out.println(String.format("middleArray: '(%s)%s'", middleIndex, Arrays.toString(middleArray)));
			System.out.println(String.format("rightArray: '(%s)%s'", rightIndex, Arrays.toString(rightArray)));
			System.out.println(String.format("result: '(%s)%s'", resultArray.length, Arrays.toString(resultArray)));
		}

		return resultArray;
	}

	private static int[] putValue(int[] array, int index, int value) {
		if (index < 0) {
			throw new RuntimeException("index need > 0");
		}

		if (index >= array.length) {
			// 扩容
			int[] tmp = new int[array.length / 2 * 3 + 2];
			System.arraycopy(array, 0, tmp, 0, array.length);
			if (debug) {
				System.out.println(String.format("扩容: %s -> %s", array.length, tmp.length));
			}
			array = tmp;
		}
		array[index] = value;
		return array;
	}

	private static int[] createNewTempArray(int length) {
		return new int[length / 3 + 1];
	}

}
