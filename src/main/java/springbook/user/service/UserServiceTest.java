package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired
	UserService userService;
	
	List<User> users;
	@Before
	public void setUp() {
		users = Arrays.asList(//배열을 리스트로 만들어주는 메소드 배열을 가변인자로 넣어주면 더욱 편리
				new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0)
				, new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0)
				, new User("erwins", "신승한", "p3", Level.SILVER, 60, 29)
				, new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30)
				, new User("green", "오민규", "p5", Level.GOLD, 100, 100)
		);
	}
	
}
