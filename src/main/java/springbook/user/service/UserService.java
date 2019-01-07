package springbook.user.service;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Test
	public void upgradeLevels() {
		List<User> users = userDao.getAll();
		for(User user : users) {
			Boolean changed = null;
			if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
				user.setLevel(Level.SILVER);
				changed = true;
			} else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
				user.setLevel(Level.GOLD);
				changed = true;
			} else if(user.getLevel() == Level.GOLD) { changed = false; }
			else { changed = false; }
			if(changed) { userDao.update(user); }
		}
	}
	
}
