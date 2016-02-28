package ziv.java8.repeatableannotation;

import java.lang.annotation.Repeatable;

import org.junit.Test;

import ziv.java8.BaseTest;

/**
 * Java 8允许我们把同一个类型的注解使用多次，只需要给该注解标注一下@Repeatable即可。<br/>
 * 另外Java 8的注解还增加到两种新的target上了：
 *
 * <pre>
 * {@code
 * @Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
 * @interface MyAnnotation {}
 * }
 * </pre>
 *
 * @author Booynal
 *
 */
@HintOld("test")
public class RepeatableAnnotationTest extends BaseTest {

	/**
	 * 第二个例子里java编译器会隐性的帮你定义好@Hints注解，了解这一点有助于你用反射来获取这些信息：
	 */
	@Test
	public void testReflect() {
		HintNew hint = PersonNew.class.getAnnotation(HintNew.class);
		System.out.println(hint); // null
		System.out.println(PersonNew.class.getAnnotations().length);
		System.out.println(PersonNew.class.getAnnotatedInterfaces().length);
		// FIXME 经过我的实测，在我的台式电脑以下代码返回null，未解
		// HintsNew hints1 = PersonNew.class.getAnnotation(HintsNew.class);
		// System.out.println(hints1.value().length); // 2
		HintNew[] hints2 = PersonNew.class.getAnnotationsByType(HintNew.class);
		System.out.println(hints2.length); // 2
	}

}

/**
 * 老方法：使用包装类当容器来存多个注解
 */
@HintsOld({ @HintOld("hit1"), @HintOld("hint2") })
class PersonOld {

}

@interface HintsOld {

	HintOld[] value();

}

@interface HintOld {

	String value();
}

/**
 * 新方法：使用多重注解
 */
@HintNew("hint1")
@HintNew("hint2")
class PersonNew {

}

@interface HintsNew {

	HintNew[] value();

}

@Repeatable(value = HintsNew.class)
@interface HintNew {

	String value();
}
