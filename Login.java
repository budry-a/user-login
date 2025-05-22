import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class is responsible for managing user's credentials
 */
public class Login {

	private HashMap<String, Credentials> credentials = new HashMap<>();

	
	/**
	 * Add provided credentials to the hashmap. Only hashed password is stored.
	 * 
	 * @param userName - the username
	 * @param password - the hashed password
	 */
	public void addCredentials(String username, String password) {
		credentials.put(username, new Credentials(username, password));
	}

	/**
	 * Check if credentials are correct
	 * 
	 * @param userName - the username to check
	 * @param password - the password
	 * @return true if correct, false if incorrect
	 */
	public boolean isValid(String username, String password) {
		Credentials credential = credentials.get(username);
		return credential!=null && credential.isPasswordValid(password);
	}

	/**
	 * Read credentials from a file
	 * 
	 * @param filename file to read from
	 */
	public void loadFile(String filename) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			String username;
			String password;
			while ((line = reader.readLine()) != null) {
				if (line.isBlank()) {
					username = reader.readLine();
					password = reader.readLine();
				} else {
					username = line;
					password = reader.readLine();
				}
				username = username.replace("username: ", "");
				password = password.replace("password: ", "");
				addCredentials(username, password);
			}
		} catch (IOException e) {
			System.err.println("Unable to read file");
		}

	}
	
	

	/**
	 * Where the program starts
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Login login = new Login();

		// some sample credentials to test with
//		login.addCredentials("real username", "real password");
//		login.addCredentials("hacker", "123");
//		login.addCredentials("hello", "world");
//		login.addCredentials("bye", "bye");

		// add credentials from file
		login.loadFile("src/passwords.txt");

		// get username and password from user
		System.out.print("Enter your user name: ");
		String username = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();

		// if username and password are correct, then grant access
		if (login.isValid(username, password)) {
			System.out.println("Access granted!");
		} else {
			System.out.println("Access denied!");
		}

		scanner.close();
	}
}
