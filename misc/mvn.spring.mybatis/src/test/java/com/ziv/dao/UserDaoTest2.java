package com.ziv.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ziv.dao.dto.User;

/**
 * 这是采用自己启动改一个context的方式来测试<br/>
 * 注意：需要自己创建user表
 *
 * @author Booynal
 *
 */
public class UserDaoTest2 {

	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		context.start();
		UserDao userDao = context.getBean(UserDao.class);
		System.out.println("UserDaoTest2.test()");
		System.out.println(userDao);
		List<User> queryAll = userDao.queryAll();
		System.out.println(queryAll);
	}

}
