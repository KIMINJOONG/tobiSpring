package springbook.user.service;


import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.RuntimeErrorException;

import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
	UserDao userDao;
//	UserLevelUpgradePolicy userLevelUpgradePolicy = new NUserLevelUpgradePolicy();
	private PlatformTransactionManager transactionManager;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}



	@Test
	public void upgradeLevels() throws Exception {
		TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			List<User> users = userDao.getAll();
			for(User user : users) {
				if(canUpgradeLevel(user)) {
					upgradeLevel(user);
				}
			}
			this.transactionManager.commit(status);
		} catch(Exception e) {
			this.transactionManager.rollback(status);
			throw e;
		} 
		
	}
	
	public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
	public static final int MIN_RECCOMEND_FOR_GOLD = 30;
	private boolean canUpgradeLevel(User user) {
		Level currentLevel = user.getLevel();
		switch (currentLevel) {
		case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
		case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
		case GOLD : return false;
		default : throw new IllegalArgumentException("Unknown Level : " + currentLevel);
		}
	}
	
	
	protected void upgradeLevel(User user) {
		user.upgradeLevel();
		userDao.update(user);
		sendUpgradeEmail(user);
	}
	
	private void sendUpgradeEmail(User user) {
		final String userId = "이메일";
		final String password = "비밀번호";
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.naver.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.naver.com");
		
		Session s = Session.getDefaultInstance(props, new Authenticator() {
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() { 
				return new javax.mail.PasswordAuthentication(userId, password); }

			
		});
		
		MimeMessage message = new MimeMessage(s);
		try {
			message.setFrom(new InternetAddress("dlswnd4452@naver.com"));
			System.out.println(user.getEmail());
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			message.setSubject("Upgrade 안내");
			message.setText("사용자님의 등급이" + user.getLevel().name() + "로 업그레이드 되었습니다.");
			
			Transport.send(message);
		} catch(AddressException e) {
			throw new RuntimeException(e);
		} catch(MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void add(User user) {
		if(user.getLevel() == null) user.setLevel(Level.BASIC);
		userDao.add(user);
	}
	
}

