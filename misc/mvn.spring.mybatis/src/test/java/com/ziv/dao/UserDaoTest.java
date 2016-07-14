package com.ziv.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ziv.dao.dto.User;

/**
 * 这是采用了Spring自带的测试工具来测试<br/>
 * 注意：需要自己创建user表
 *
 * @author Booynal
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void test() {
		System.out.println("UserDaoTest.test()");
		System.out.println(userDao);
		List<User> queryAll = userDao.queryAll();
		System.out.println(queryAll);
	}

}
