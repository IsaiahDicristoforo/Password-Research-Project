package DictionaryAttack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class DictionaryAttackResult {
	
	private int totalWordsChecked;
	
	private ArrayList<PasswordResult> crackedPasswords;
	private ReentrantLock totalWordsCheckedLock;
	private int totalWordsWithStringNumberCombination;
	private HashMap<Integer, Integer> wordLengthDistribution;
	
	public DictionaryAttackResult(){
		totalWordsCheckedLock = new ReentrantLock();
		totalWordsChecked = 0;
		totalWordsWithStringNumberCombination = 0;
		this.wordLengthDistribution = new HashMap<Integer, Integer>();
	}
	
	
	
	
	public void AddPasswordResult(PasswordResult passwordResult) {
		
		totalWordsCheckedLock.lock();
		try {
			
			totalWordsChecked = totalWordsChecked + 1;
			
			if(passwordResult.isCracked()) {
				addWordToLengthTracker(passwordResult.getPlainTextPassword());
				if(passwordResult.isStringNumberCombination()) {
					this.totalWordsWithStringNumberCombination++;
				}
			}
			

			//System.out.println(passwordResult.getPlainTextPassword() + " starts with lowercase letter " + passwordResult.BeginsWithLowercaseLetter());
		
		}finally {
			totalWordsCheckedLock.unlock();

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

}
