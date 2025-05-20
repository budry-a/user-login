import java.util.HashMap;
import java.util.Scanner;

public class Login {

	private HashMap<String, String> credentials = new HashMap<>();
	
	
	void add_credentials(String userName, String password) {
		credentials.put(userName, password);
	}
	
	boolean is_valid(String userName, String password) {
		
		if(credentials.containsKey(userName)) {
			if(credentials.get(userName).equals(password)){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Login login = new Login(); 
		
		login.add_credentials("real username", "real password");
		login.add_credentials("hacker", "123");
		login.add_credentials("hello", "world");
		login.add_credentials("bye", "bye");
		
		System.out.print("Enter your user name: ");
		String userName = scanner.nextLine();
		System.out.print("Enter your password: ");
		String password = scanner.nextLine();
		
		if(login.is_valid(userName, password)) {
			System.out.println("Access granted!");
		} else {
			System.out.println("Access denied!");
		}
	}

}
