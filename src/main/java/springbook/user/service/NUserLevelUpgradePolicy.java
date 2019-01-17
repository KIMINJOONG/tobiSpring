package springbook.user.service;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;

import org.springframework.beans.factory.annotation.Autowired;

import springbook.user.dao.UserDao;

public class NUserLevelUpgradePolicy implements UserLevelUpgradePolicy{

	@Override
	public boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD : return false;
		default : throw new IllegalArgumentException("Unknown Level : " + currentLevel);
		}
	}

	@Override
	public void upgradeLevel(User user, UserDao userDao) {
		user.upgradeLevel();
		userDao.update(user);
	}

}
