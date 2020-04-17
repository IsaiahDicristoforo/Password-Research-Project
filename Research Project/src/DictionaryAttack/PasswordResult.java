package DictionaryAttack;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nulabinc.zxcvbn.Feedback;
import com.nulabinc.zxcvbn.Zxcvbn;

public class PasswordResult {
	
	private String plainTextPassword;
	private boolean isCracked;
	
	private boolean beginsWithNumber = false;
	private boolean endsWithNumber = false;
	private boolean startsWithLetter = false;
	private boolean endsWithLetter = false;
	private boolean containsAllNumbers = false;
	private boolean containsAllLetters= false;
	private boolean isStringNumberCombination = false;
	private boolean containsSpecialCharacter = false;
	private int totalLetters = 0;
	private int totalNumbers = 0;
	private int totalSpecialCharacters = 0;
	private int totalUppercaseLetters = 0;
	private int totalLowercaseLetters = 0;
	private boolean beginsWithUppercaseLetter = false;
	private boolean beginsWithLowercaseLetter;
	private int passwordStrength;
	public String passwordFeedback;
	
	
	


	public PasswordResult(){

		plainTextPassword = null;
		isCracked = false;
		
	}
	

	public String getPlainTextPassword() {
		return new String(plainTextPassword);
	}

	/**
	 * This method is called if we have cracked the hashed password
	 * @param result The plaintext password we have cracked
	 */
	public void setPlainTextPassword(String result) {
		this.plainTextPassword = result;
		
		if(result != null) {
			this.setBeginsWithLowercaseLetter();
			this.setBeginsWithNumber();
			this.setContainsAllLetters();
			this.setContainsAllNumbers();
			this.setEndsWithLetter();
			this.setEndsWithNumber();
			this.setStringNumberCombination();
			this.setBeginsWithUppercaseLetter();
			this.setStartsWithLetter();
			this.setTotalLetters();
			this.setTotalLowercaseLetters();
			this.setTotalNumbers();
			this.setTotalUppercaseLetters();
			
			Zxcvbn strengthCalculator = new Zxcvbn();
			int strength = strengthCalculator.measure(result).getScore();
			this.setPasswordStrength(strength);
			
			Feedback feedback = strengthCalculator.measure(result).getFeedback();
			List<String> passwordFeedback = feedback.getSuggestions();
	
			this.passwordFeedback = "";
			for(String s : passwordFeedback) {
				this.passwordFeedback += s + " ";
			}
			
		}
		
	}
	
	public boolean isCracked() {
		return isCracked;
	}
	public void isCracked(boolean isCracked) {
		this.isCracked = isCracked;
	}
	public String toString() {
		return "Password: " + plainTextPassword + "  Length: " + plainTextPassword.length();
	}


	public boolean BeginsWithNumber() {
		return beginsWithNumber;
	}


	private void setBeginsWithNumber() {
		this.beginsWithNumber = Character.isDigit(this.plainTextPassword.charAt(0));
	}


	public boolean endsWithNumber() {
		return endsWithNumber;
	}


	private void setEndsWithNumber() {
		this.endsWithNumber = Character.isDigit(this.plainTextPassword.charAt(this.plainTextPassword.length() - 1));
	}


	public boolean isStartsWithLetter() {
		return startsWithLetter;
	}


	private void setStartsWithLetter() {
		this.startsWithLetter = Character.isLetter(this.plainTextPassword.charAt(0));
	}


	public boolean EndsWithLetter() {
		return endsWithLetter;
	}


	private void setEndsWithLetter() {
		this.endsWithLetter = Character.isLetter(this.plainTextPassword.charAt(this.plainTextPassword.length()-1));
	}


	public boolean ContainsAllNumbers() {
		return containsAllNumbers;
	}


	private void setContainsAllNumbers() {
		try {
			Integer.parseInt(this.plainTextPassword);
			this.containsAllNumbers = true;
		}catch(Exception e) {
			this.containsAllNumbers = false;
		}
		
	}


	public boolean ContainsAllLetters() {
		return containsAllLetters;
	}


	private void setContainsAllLetters() {

		this.containsAllLetters = true;
		for(char c : this.plainTextPassword.toCharArray()) {
			if(!Character.isLetter(c)) {
				this.containsAllLetters = false;
			}
		}

	}


	public boolean isStringNumberCombination() {
		return isStringNumberCombination;
	}


	private void setStringNumberCombination() {
		boolean combination = true;
		for(int i  = 0; i < this.plainTextPassword.length();i++) {
			if(Character.isDigit(plainTextPassword.charAt(i)) && i > 0){
				String possibleLetters = this.plainTextPassword.substring(0,i);
				for(int j = 0; j < possibleLetters.length(); j++) {
					if(Character.isDigit(possibleLetters.charAt(j)) || !Character.isLetter(possibleLetters.charAt(j))) {
						combination = false;
					}
				}
				try {
					Integer.parseInt(plainTextPassword.substring(i,plainTextPassword.length()));
					if(combination == true) {
						this.isStringNumberCombination = true;
						break;
					}
				}catch(Exception e) {
					combination = false;
				}
			}
		}
		
	
		
	}


	public int getTotalLetters() {
		return totalLetters;
	}


	private void setTotalLetters() {
		int totalLettersInWord = 0;
		for(char c : plainTextPassword.toCharArray()) {
			if(Character.isLetter(c)) {
				totalLettersInWord++;
			}
		}
		this.totalLetters = totalLettersInWord;
	}


	public int getTotalNumbers() {
		return totalNumbers;
	}


	private void setTotalNumbers() {
		for(char c : plainTextPassword.toCharArray()) {
			if(Character.isDigit(c)) {
				this.totalNumbers++;
			}
		}
	}


	public int getTotalUppercaseLetters() {
		return totalUppercaseLetters;
	}


	private void setTotalUppercaseLetters() {
		for(char c : plainTextPassword.toCharArray()) {
			if(Character.isUpperCase(c)) {
				this.totalUppercaseLetters++;
			}
		}
	}


	public int getTotalLowercaseLetters() {
		return totalLowercaseLetters;
	}


	private void setTotalLowercaseLetters() {
		
		for(char c : plainTextPassword.toCharArray()) {
			if(Character.isLowerCase(c)) {
				this.totalLowercaseLetters++;
			}
		}
	}


	public boolean BeginsWithUppercaseLetter() {
		return beginsWithUppercaseLetter;
	}


	private void setBeginsWithUppercaseLetter() {
		this.beginsWithUppercaseLetter = Character.isUpperCase(plainTextPassword.charAt(0));
	}


	public boolean BeginsWithLowercaseLetter() {
		return beginsWithLowercaseLetter;
	}


	private void setBeginsWithLowercaseLetter() {
		this.beginsWithLowercaseLetter = Character.isLowerCase(plainTextPassword.charAt(0));
	}


	public int getPasswordStrength() {
		return passwordStrength;
	}


	public void setPasswordStrength(int passwordStrength) {
		this.passwordStrength = passwordStrength;
	}
	
	public String getPasswordFeedback() {
		return passwordFeedback;
	}


	public void setPasswordFeedback(String passwordFeedback) {
		this.passwordFeedback = passwordFeedback;
	}

	

}
