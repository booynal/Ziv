package mvn.spring.aop.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Booynal
 * @Aspect注解直接作用在切面类，同时切指定的注解(@EnableLogException)
 */
@Component
@Aspect
public class MyAnnotationAdvice2 {

	/** 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点 **/
	@Pointcut("@annotation(EnableLogException)")
	public void pointcutInThisClass() {
	}

	/** 配置前置通知,使用在方法aspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数 **/
	@Before("pointcutInThisClass()")
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

	/** 配置后置通知,使用在方法aspect()上注册的切入点 **/
	@After("pointcutInThisClass()")
	public void afterMethod(JoinPoint jpoint) {
		System.out.println("after method: " + jpoint.getSignature().toShortString());
	}

	/** 配置后置返回通知,使用在方法aspect()上注册的切入点 **/
	@AfterReturning("pointcutInThisClass()")
	public void afterReturningMethod(JoinPoint jpoint) {
		System.out.println("afterReturning method: " + jpoint.getSignature().toShortString());
	}

	/** 配置抛出异常后通知,使用在方法aspect()上注册的切入点 **/
	@AfterThrowing(pointcut = "pointcutInThisClass()", throwing = "ex")
	public void afterThrowingMethod(JoinPoint jpoint, Exception ex) {
		System.out.println("afterThrowing method: " + jpoint.getSignature().toShortString());
	}

	/**
	 * 配置环绕通知,使用在方法aspect()上注册的切入点
	 *
	 * @param jpoint
	 * @return 返回所代理方法的返回值
	 * @throws Throwable
	 */
	@Around("pointcutInThisClass()")
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
