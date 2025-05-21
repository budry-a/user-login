import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class Login {

	private HashMap<String, String> credentials = new HashMap<>();

	/**
	 * Compute the SHA256 hash of the provided password
	 * 
	 * @param password the password provided
	 * @return a string representing the hash
	 */
	String SHA(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Add provided credentials to the hashmap. Only hashed password is stored.
	 * 
	 * @param userName - the username
	 * @param password - the hashed password
	 */
	void addCredentials(String userName, String password) {
		credentials.put(userName, SHA(password));
	}

	/**
	 * Check if credentials are correct
	 * 
	 * @param userName - the username to check
	 * @param password - the password
	 * @return true if correct, false if incorrect
	 */
	boolean isValid(String userName, String password) {
		if (credentials.containsKey(userName)) {
			if (credentials.get(userName).equals(SHA(password))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Read credentials from a file
	 * 
	 * @param filename file to read from
	 */
	void loadFile(String filename) {
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
		String userName = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();

		// if username and password are correct, then grant access
		if (login.isValid(userName, password)) {
			System.out.println("Access granted!");
		} else {
			System.out.println("Access denied!");
		}

		scanner.close();
	}
}
