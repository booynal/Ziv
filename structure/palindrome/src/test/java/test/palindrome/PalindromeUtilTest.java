package test.palindrome;

import org.junit.Assert;
import org.junit.Test;

/**
 * 创建人:  ziv
 * 创建时间: 2018/4/6 11:10
 */
public class PalindromeUtilTest {

	@Test
	public void isPalindrome() {
		boolean isPalindrome;
		System.out.println(isPalindrome = PalindromeUtil.isPalindrome("aba"));
		Assert.assertTrue(isPalindrome);
		System.out.println(isPalindrome = PalindromeUtil.isPalindrome("abac"));
		Assert.assertFalse(isPalindrome);
		System.out.println(isPalindrome = PalindromeUtil.isPalindrome("abacaba"));
		Assert.assertTrue(isPalindrome);
		System.out.println(isPalindrome = PalindromeUtil.isPalindrome("abaccaba"));
		Assert.assertTrue(isPalindrome);
	}
}
