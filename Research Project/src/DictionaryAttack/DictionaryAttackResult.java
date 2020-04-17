package DictionaryAttack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.nulabinc.zxcvbn.Zxcvbn;

public class DictionaryAttackResult {
	
	private int totalWordsChecked;
	
	private ArrayList<PasswordResult> crackedPasswords;
	private ReentrantLock totalWordsCheckedLock;
	private int totalWordsWithStringNumberCombination;
	private int totalBeginningWithLowercaseLetter;
	private int totalBeginningWithUppercaseLetter;
	private HashMap<Integer, Integer> wordLengthDistribution;
	private int totalCracked = 0;
	private int strength1;
	private int strength2;
	private int strength3;
	private int strength4;
	
	public DictionaryAttackResult(){
		totalWordsCheckedLock = new ReentrantLock();
		totalWordsChecked = 0;
		totalWordsWithStringNumberCombination = 0;
		this.wordLengthDistribution = new HashMap<Integer, Integer>();
	}
	
	
	
	
	public synchronized void AddPasswordResult(PasswordResult passwordResult) {
		
		//totalWordsCheckedLock.lock();
		try {
			
			
			totalWordsChecked = totalWordsChecked + 1;
			
			
			if(passwordResult.isCracked()) {
				totalCracked++;
				
				if(passwordResult.getPasswordStrength() == 1) {
					this.strength1++;
				}
				if(passwordResult.getPasswordStrength() == 2) {
					this.strength2++;
				}
				if(passwordResult.getPasswordStrength() == 3) {
					this.strength3++;
				
				}
				if(passwordResult.getPasswordStrength() == 4) {
					this.strength4++;
				}

				
				addWordToLengthTracker(passwordResult.getPlainTextPassword());
				if(passwordResult.isStringNumberCombination()) {
					this.totalWordsWithStringNumberCombination++;
				}
				if(passwordResult.BeginsWithLowercaseLetter()) {
					this.totalBeginningWithLowercaseLetter++;
				}
				if(passwordResult.BeginsWithUppercaseLetter()) {
					this.totalBeginningWithUppercaseLetter++;
				}
				
			}
			

			//System.out.println(passwordResult.getPlainTextPassword() + " starts with lowercase letter " + passwordResult.BeginsWithLowercaseLetter());
		
		}finally {
			//totalWordsCheckedLock.unlock();

		}
		
	}
	
	private void addWordToLengthTracker(String password) {

		int length = password.length();

		if (this.wordLengthDistribution.containsKey(length)) {

			this.wordLengthDistribution.replace(length, this.wordLengthDistribution.get(length) + 1);
		} else {
			this.wordLengthDistribution.put(password.length(), 1);
		}

	}
	
	
	public int getTotalWordsChecked() {
		return this.totalWordsChecked;
	}
	
	


	public int getTotalWordsWithStringNumberCombination() {
		return totalWordsWithStringNumberCombination;
	}




	public void setTotalWordsWithStringNumberCombination(int totalWordsWithStringNumberCombination) {
		this.totalWordsWithStringNumberCombination = totalWordsWithStringNumberCombination;
	}




	public HashMap<Integer, Integer> getWordLengthDistribution() {
		return wordLengthDistribution;
	}




	public void setWordLengthDistribution(HashMap<Integer, Integer> wordLengthDistribution) {
		this.wordLengthDistribution = wordLengthDistribution;
	}




	public int getTotalCracked() {
		return totalCracked;
	}




	public void setTotalCracked(int totalCracked) {
		this.totalCracked = totalCracked;
	}

	public int getTotalBeginningWithUppercaseLetter() {
		return this.totalBeginningWithUppercaseLetter;
	}



	public int getTotalBeginningWithLowercaseLetter() {
		return totalBeginningWithLowercaseLetter;
	}




	public void setTotalBeginningWithLowercaseLetter(int totalBeginningWithLowercaseLetter) {
		this.totalBeginningWithLowercaseLetter = totalBeginningWithLowercaseLetter;
	}

}
