import java.io.Console;
import java.util.Scanner;

/**
 * This class asks for user credentials and grants access
 *  javac -cp ".;C:\SQLite JDBC\sqlite-jdbc-3.49.1.0.jar" Login.java
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
		String username;
		db.checkDBExists();
		db.connect();
		db.createTable();
		while(true) {
			try {
				System.out.println("Login(1), Signup(2), Exit(3)?");
				choice = Integer.parseInt(scanner.nextLine());
				if(choice<1||choice>3) throw new NumberFormatException();
			} catch (NumberFormatException e) {
				System.err.println("Please enter 1 for login, 2 for signup or 3 to exit");
				continue;
			}
			if(choice==3) {
				break;
			}
			// get username and password from user
			System.out.print("Enter your user name: ");
			username = scanner.nextLine();
			Console console = System.console();
			char[] password = console.readPassword("Enter your password: ");
			credential = new Credentials(username, String.valueOf(password));
			
			if(choice==1) {
				// if username and password are correct, then grant access
				String hashedPass = db.getCredentials(username);
				if (hashedPass!=null && login.isValid(username, String.valueOf(password), credential)) {
					System.out.println("Access granted!");
				} else {
					System.out.println("Access denied!");
				}
			} else if(choice==2) {
				db.addUser(credential);
			} 
			
		}
		scanner.close();
		db.disconnect();
		System.out.println("Goodbye");
	}
}
