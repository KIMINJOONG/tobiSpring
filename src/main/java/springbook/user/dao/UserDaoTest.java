package springbook.user.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import javax.activation.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserDaoTest {
	
//	private ApplicationContext context;
	UserDao dao;
	private User user1;
	private User user2;
	private User user3;
	
	@Before
	public void setUp() {
//		this.dao = this.context.getBean("userDao", UserDao.class);
		dao = new UserDao();
		javax.sql.DataSource dataSource = new SingleConnectionDataSource(
				"jdbc:mysql://localhost/testdb?serverTimezone=UTC", "spring", "book", true);
		dao.setDataSource(dataSource);
		this.user1 = new User("gyumee", "�ڼ�ö", "springno1");
		this.user2 = new User("leegw700", "�̱��", "springno2");
		this.user3 = new User("bumjin", "�ڹ���", "springno3");
	}
	
	@Test
	public void addAndGet() throws SQLException, ClassNotFoundException {
		User user1 = new User("gyumee", "�ڼ�ö", "springno1");
		User user2 = new User("leegw700", "�̱��", "springno2");
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		
		
		dao.add(user1);
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		User userget1 = dao.get(user1.getId());
		assertThat(userget1.getName(), is(user1.getName()));
		assertThat(userget1.getPassword(), is(user1.getPassword()));
		
		User userget2 = dao.get(user2.getId());
		assertThat(userget2.getName(), is(user2.getName()));
		assertThat(userget2.getPassword(), is(user2.getPassword()));
	}
	
	@Test
	public void count() throws SQLException, ClassNotFoundException {
		User user1 = new User("gyumee", "�ڼ�ö", "springno1");
		User user2 = new User("leegw700", "�̱��", "springno2");
		User user3 = new User("bumjin", "�ڹ���", "springno3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getUserFailure() throws SQLException, ClassNotFoundException {
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		dao.get("unkown_id");
	}
}
