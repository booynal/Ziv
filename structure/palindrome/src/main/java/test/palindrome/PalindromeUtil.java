package test.palindrome;

/**
 * 类描述:  回文测试
 * 创建人:  ziv
 * 创建时间: 2018/4/6 10:54
 */
public class PalindromeUtil {

	/**
	 * 方法描述: 判断一个字符串是否为回文字符串
	 *
	 * @param string 要进行判断的字符串
	 * @return 如果测试结果是回文字符串，则返回true，否则返回false
	 * @author ziv
	 * @date 2018/4/6 11:03
	 */
	public static boolean isPalindrome(String string) {
		if (null == string || string.length() < 2) {
			return false;
		}

		char[] chars = string.toCharArray();
		for (int i = 0, j = string.length() - 1; i < j; i++, j--) {
			if (chars[i] != chars[j]) {
				return false;
			}
		}
		return true;
	}
}
