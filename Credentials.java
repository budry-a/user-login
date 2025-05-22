import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class represents a users credentials, and is responsible for validating a password against its hash
 */
public class Credentials {
	
	/** Stores username */
	private String username;
	/** Stores the hash of the password */
	private String passwordHash;
	
	/**
	 * Constructor which initializes username and passwordHash
	 * @param username
	 * @param password
	 */
	public Credentials(String username, String password) {
		this.username=username;
		this.passwordHash=hashPassword(password);
	}
	
	/**
	 * Compute the SHA256 hash of the provided password
	 * 
	 * @param password the password provided
	 * @return a string representing the hash
	 */
	public String hashPassword(String password) {
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
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Accessor for username
	 * 
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Compares the hash of the password given to the password hash saved
	 * 
	 * @param password the password given
	 * @return true if password given has the same hash and passwordHash
	 */
	public boolean isPasswordValid(String password) {
		return hashPassword(password).equals(passwordHash);
	}
	
}
