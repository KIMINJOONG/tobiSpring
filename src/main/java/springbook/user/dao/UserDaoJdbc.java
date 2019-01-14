package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.cj.exceptions.MysqlErrorNumbers;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao{
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private	JdbcTemplate jdbcTemplate;
	private RowMapper<User> userMapper = 
			new RowMapper<User>() {
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User user = new User();
					user.setId(rs.getString("id"));
					user.setName(rs.getString("name"));
					user.setPassword(rs.getString("password"));
					user.setLevel(Level.valueOf(rs.getInt("level")));
					user.setLogin(rs.getInt("login"));
					user.setRecommend(rs.getInt("recommend"));
					user.setEmail(rs.getString("email"));
					return user;
				}
			};
	
	
	public void add(final User user) throws DuplicateUserIdException {
		try {
			this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend, email) values(?,?,?,?,?,?,?)", 
					user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail());
		}catch (DuplicateUserIdException e) {
			throw new DuplicateUserIdException(e);
		}
		
	}
	
	public User get(String id) {
		return this.jdbcTemplate.queryForObject("select * from users where id= ?", 
				new Object[] {id}, this.userMapper);
	}
	
	public void deleteAll() {
		this.jdbcTemplate.update("delete from users");
	}
	
	
	private PreparedStatement makeStatement(Connection c) throws SQLException {
		PreparedStatement ps;
		ps = c.prepareStatement("delete from users");
		return ps;
	}
	
	public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
//			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		} catch(SQLException e) {
			throw e;
		} finally {
			if(ps != null) { try {ps.close();} catch(SQLException e) {} }
			if(c != null) {try{c.close();} catch(SQLException e) {} }
		}
	}
	
	public int getCount() {
		return this.jdbcTemplate.query(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("select count(*) from users");
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet rs) throws SQLException,DataAccessException {
					rs.next();
					return rs.getInt(1);
			}
		});
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query("select * from users order by id", 
				this.userMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update("update users set name = ?, password = ?"
				+ ", level =?, login=?, recommend =?, email = ? where id =? ",user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(),user.getEmail(), user.getId());
		
	}
	
}
