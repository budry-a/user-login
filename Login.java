import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

public class Login {

	private HashMap<String, String> credentials = new HashMap<>();
	
	/**
	 * Compute the SHA256 hash of the provided password
	 * @param password the password provided
	 * @return a string representing the hash
	 */
	String SHA(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for(byte b:digest) {
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
	 * @param userName - the username
	 * @param password - the hashed password
	 */
	void add_credentials(String userName, String password) {
		credentials.put(userName, SHA(password));
	}
	
	/**
	 * Check if credentials are correct
	 * @param userName - the username to check
	 * @param password - the password
	 * @return true if correct, false if incorrect
	 */
	boolean is_valid(String userName, String password) {
		
		if(credentials.containsKey(userName)) {
			if(credentials.get(userName).equals(SHA(password))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Where the program starts
	 * 
	 * @param args  not used
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Login login = new Login(); 
		
		// some sample credentials to test with
		login.add_credentials("real username", "real password");
		login.add_credentials("hacker", "123");
		login.add_credentials("hello", "world");
		login.add_credentials("bye", "bye");
		
		// get username and password from user
		System.out.print("Enter your user name: ");
		String userName = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();
		
		// if username and password are correct, then grant access
		if(login.is_valid(userName, password)) {
			System.out.println("Access granted!");
		} else {
			System.out.println("Access denied!");
		}
		
		scanner.close();
	}
}
