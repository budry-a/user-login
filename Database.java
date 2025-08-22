import java.io.Console;
import java.io.File;
import java.sql.*;
import java.util.Scanner;

import org.sqlite.SQLiteDataSource;

public class Database {

	private Connection connection;
	private String dbURL;
	private SQLiteDataSource dataSource;
	private Scanner scanner = new Scanner(System.in);
	
	public Database() {
		connection = null;
		dbURL = "jdbc:sqlite:credentialsDB";
		dataSource = new SQLiteDataSource();
	}
	
	public void checkDBExists() {
		File dbFile = new File("credentialsDB");
		if (dbFile.exists()) {
			System.out.println("File already exists, do you want to delete? y/n");
			String option = scanner.nextLine();
			if (option.equalsIgnoreCase("y")) {
				dbFile.delete();
			}
		}
	}
	
	public void connect() {
		dataSource.setUrl(dbURL);
		try {
			connection = dataSource.getConnection();
			System.out.println("Connection established");
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	
	public void createTable() {
		String addTable = "CREATE TABLE IF NOT EXISTS users (username VARCHAR, password_hash VARCHAR);";
		try (Statement s1 = connection.createStatement();) {
			s1.execute(addTable);
		} catch (SQLException e) {
			System.err.println("error creating table");
			e.printStackTrace();
		}
	}
	
	public void addUser(Credentials credential) {
		String username = credential.getUsername();
		String verifyUser = "SELECT * FROM users WHERE username = ?;";
		try(PreparedStatement s = connection.prepareStatement(verifyUser)){
			s.setString(1, username);
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				System.out.println("Username exists!");
			} else {
				String password = credential.getPasswordHash();
				String insert = "INSERT INTO users VALUES(?,?);";
				try (PreparedStatement ps = connection.prepareStatement(insert)) {
					ps.setString(1, username);
					ps.setString(2, password);
					ps.executeUpdate();
				} catch (SQLException e) {
					System.err.println("adding user error ");
					e.printStackTrace();
				}
				System.out.println("Successfully added user!");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public String getCredentials(String username) {
		String hashedPass = null;
		String view = "SELECT * FROM users WHERE username = ?;";
		try (PreparedStatement s3 = connection.prepareStatement(view)) {
			s3.setString(1,username);
			ResultSet rs = s3.executeQuery();
			int rows = rs.getRow();
			if(rows==0) {
				return null;
			}
			hashedPass = rs.getString(2);
		} catch (SQLException e) {
			System.err.println("error viewing table");
			e.printStackTrace();
		}
		return hashedPass;
	}
}
