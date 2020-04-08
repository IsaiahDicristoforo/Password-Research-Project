package DictionaryAttack;

public class PasswordResult {
	
	private String plainTextPassword;
	boolean isCracked;
	

	public String getPlainTextPassword() {
		return plainTextPassword;
	}

	public void setPlainTextPassword(String result) {
		this.plainTextPassword = result;
	}
	
	public String toString() {
		return "Password: " + plainTextPassword + "  Length: " + plainTextPassword.length();
	}
	public boolean isCracked() {
		return isCracked;
	}
	public void isCracked(boolean isCracked) {
		this.isCracked = isCracked;
	}

}
