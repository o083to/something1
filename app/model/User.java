package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import play.db.*;
import utils.CommonUtils;

public class User {
	
	private static final String GET_USER_BY_LOGIN = "select login, password from ib_lab_users where login = ?";
	private static final String INSERT_NEW_USER = "insert into ib_lab_users (login, password) values (?, ?)";
	
	private String login;
	
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public User(String login) {
		this.login = login;
	}
	
	public static User authenticate(String login, String password) {
		if (login == null || password == null) return null;
		DataSource dataSource = DB.getDataSource();
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement query = connection.prepareStatement(GET_USER_BY_LOGIN)
			) {			
				query.setString(1, login);
				ResultSet result = query.executeQuery();
				if (result.next() && CommonUtils.getSha1HashCode(password).equals(result.getString("password"))) {
					return new User(login);
				}			
			} catch (SQLException e) {
				// TODO do something
				e.printStackTrace();
			}		
        return null;
    }
	
	public static boolean add(String login, String password) {
		DataSource dataSource = DB.getDataSource();
		try (
				Connection connection = dataSource.getConnection();
				PreparedStatement query = connection.prepareStatement(INSERT_NEW_USER)
			) {
				query.setString(1, login);
				query.setString(2, CommonUtils.getSha1HashCode(password));
				query.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO do something
				e.printStackTrace();
				return false;
			}
	}

}
