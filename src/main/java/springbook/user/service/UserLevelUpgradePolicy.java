package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.User;

public interface UserLevelUpgradePolicy {
	boolean canUpgradeLevel(User user);
	void upgradeLevel(User user, UserDao userDao);
}
