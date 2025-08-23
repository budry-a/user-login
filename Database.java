import java.io.File;
import java.sql.*;
import java.util.Scanner;
import org.sqlite.SQLiteDataSource;

/**
 * This class is responsible for creating, and connecting to a database
 */
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
	
	/**
	 * If the database already exists, prompt to delete
	 */
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
	
	/**
	 * Connect to database
	 */
	public void connect() {
		dataSource.setUrl(dbURL);
		try {
			connection = dataSource.getConnection();
			System.out.println("Connection established");
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	
	/**
	 * Create a table in the database
	 */
	public void createTable() {
		String addTable = "CREATE TABLE IF NOT EXISTS users (username VARCHAR, password_hash VARCHAR);";
		try (Statement s1 = connection.createStatement();) {
			s1.execute(addTable);
		} catch (SQLException e) {
			System.err.println("error creating table");
			e.printStackTrace();
		}
	}
	
	/**
	 * Add new user credential to database
	 * @param credential The credentials to add
	 */
	public void addUser(Credentials credential) {
		String username = credential.getUsername();
		String verifyUser = "SELECT * FROM users WHERE username = ?;";
		try(PreparedStatement s = connection.prepareStatement(verifyUser)){
			s.setString(1, username);
			ResultSet rs = s.executeQuery();
			if(rs.next()) {
				System.out.println("Username exists!"); // if username already exists, don't add
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
	
	/**
	 * Get the credentials of a user
	 * @param username The username of the user 
	 * @return The hashed password of the user, or null if user doesn't exist
	 */
	public String getCredentials(String username) {
		String hashedPass = null;
		String view = "SELECT * FROM users WHERE username = ?;";
		try (PreparedStatement s3 = connection.prepareStatement(view)) {
			s3.setString(1,username);
			ResultSet rs = s3.executeQuery();
			int rows = 0;
			if (rs.next()) {
				rows++; 
	        }
			
			if(rows==0) {  
				return null;  // if user is not in the table, return null
			}
			hashedPass = rs.getString(2);
		} catch (SQLException e) {
			System.err.println("error viewing table");
			e.printStackTrace();
		}
		return hashedPass;
	}
	
	/**
	 * Close the connection 
	 */
	public void disconnect() {
		if(connection!=null) {
			try {
				connection.close();
			} catch(SQLException e) {
				e.printStackTrace(System.err);
			}
		}
	}
}
