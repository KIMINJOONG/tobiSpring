package springbook.user.service;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import springbook.user.dao.UserDao;

public class UserService {
	UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	
}
