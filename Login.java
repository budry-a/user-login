import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class is responsible for managing user's credentials
 */
public class Login {

	/**
	 * Check if credentials are correct
	 * 
	 * @param userName - the username to check
	 * @param password - the password
	 * @return true if correct, false if incorrect
	 */
	public boolean isValid(String username, String password, Credentials credential) {
		return credential!=null && credential.isPasswordValid(password);
	}


	/**
	 * Where the program starts
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Login login = new Login();
		Database db = new Database();
		Credentials credential;
		int choice=0;
		Console console = System.console();
		String username;
		db.checkDBExists();
		db.connect();
		try {
			System.out.println("Login(1) or Signup(2)?");
			choice = Integer.parseInt(scanner.nextLine());
			if(choice!=1||choice!=2) throw new NumberFormatException();
		} catch (NumberFormatException e) {
			System.err.println("Please enter 1 for login or 2 for signup");
		}
		
		// get username and password from user
		System.out.print("Enter your user name: ");
		username = scanner.nextLine();
		char[] password = console.readPassword("\"Enter your password: ");
		credential = new Credentials(username, password.toString());
		
		if(choice==1) {
			// if username and password are correct, then grant access
			String hashedPass = db.getCredentials(username);
			if (hashedPass!=null && login.isValid(username, password.toString(), credential)) {
				System.out.println("Access granted!");
			} else {
				System.out.println("Access denied!");
			}
		} else if(choice==2) {
			db.addUser(credential);
		}
		

		scanner.close();
	}
}
