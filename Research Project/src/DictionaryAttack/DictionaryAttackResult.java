package DictionaryAttack;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class DictionaryAttackResult {
	
	private int totalWordsChecked;
	
	private ArrayList<PasswordResult> crackedPasswords;
	private ReentrantLock totalWordsCheckedLock;

	
	public DictionaryAttackResult(){
		totalWordsCheckedLock = new ReentrantLock();
		totalWordsChecked = 0;
	}
	
	
	public void incrementTotalWordsChecked() {
		totalWordsCheckedLock.lock();
		try {
			totalWordsChecked = totalWordsChecked + 1;
			
		}finally {
			totalWordsCheckedLock.unlock();

		}
	}
	
	
	public int getTotalWordsChecked() {
		return this.totalWordsChecked;
	}

}
