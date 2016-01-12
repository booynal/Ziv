package mvn.spring.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Booynal
 *
 */
public class MyAdvice {

	public void beforeMethod(JoinPoint jpoint) {
		System.out.println("before method: " + jpoint.getSignature().toShortString());
		System.err.println("kind: " + jpoint.getKind());
		System.err.println("target: " + jpoint.getTarget());
		System.err.println("this: " + jpoint.getThis());
		System.err.println("args:" + Arrays.toString(jpoint.getArgs()));
		System.err.println("signature: " + jpoint.getSignature().toLongString());
		System.err.println("location: " + jpoint.getSourceLocation());
		System.err.println("staticPart: " + jpoint.getStaticPart());
	}

	public void afterMethod(JoinPoint jpoint) {
		System.out.println("after method: " + jpoint.getSignature().toShortString());
	}

	public void afterReturningMethod(JoinPoint jpoint) {
		System.out.println("afterReturning method: " + jpoint.getSignature().toShortString());
	}

	public void afterThrowingMethod(JoinPoint jpoint) {
		System.out.println("afterThrowing method: " + jpoint.getSignature().toShortString());
	}

	/**
	 * @param jpoint
	 * @return 返回所代理方法的返回值
	 * @throws Throwable
	 */
	public Object aroundMethod(ProceedingJoinPoint jpoint) throws Throwable {
		try {
			System.out.println("around before method: " + jpoint.getSignature().toShortString());
			Object object = jpoint.proceed();
			System.out.println("around after method: " + jpoint.getSignature().toShortString());
			return object;
		} catch (Throwable e) {
			System.out.println("around afterThrowing method: " + jpoint.getSignature().toShortString());
			throw e;
		}
	}
}
