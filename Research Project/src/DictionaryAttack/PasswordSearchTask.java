package DictionaryAttack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import com.nulabinc.zxcvbn.Zxcvbn;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class PasswordSearchTask extends Task<PasswordResult> {


	private String password;
	private CountDownLatch countDownLatch;
	private File dictionary;
	private ListView lv_CrackedPasswords;
	private int totalWordsInDictionary;
	private int threadsForEachPassword;
	private Label lbl_TotalCracked;
	private Label lbl_TotalNotCracked;
	private Label lbl_s0;
	private Label lbl_s1;
	private Label lbl_s2;
	private Label lbl_s3;
	private Label lbl_s4;
	private Label[] passwordStatLabels;

	
	public PasswordSearchTask(CountDownLatch countDownLatch,String hashedPassword, File dictionary,
			ListView lv_CrackedPasswords, int totalWordsInDictionary, 
			int totalThreadsForEachPassword, Label totalCracked, Label totalFailed,Label s0, Label s1, Label s2, Label s3, Label s4, Label[] passwordStatLabels) {
		password = new String(hashedPassword);
		this.passwordStatLabels = passwordStatLabels;
		this.lbl_s0 = s0;
		this.lbl_s1 = s1;
		this.lbl_s2 = s2;
		this.lbl_s3 = s3;
		this.lbl_s4 = s4;
		this.dictionary = dictionary;
		this.lbl_TotalCracked = totalCracked;
		this.lbl_TotalNotCracked = totalFailed;
		this.totalWordsInDictionary = totalWordsInDictionary;
		this.lv_CrackedPasswords = lv_CrackedPasswords;
		this.countDownLatch = countDownLatch;
		this.threadsForEachPassword = totalThreadsForEachPassword;
		this.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				countDownLatch.countDown();
			}
		});
	}
	
	@Override
	protected PasswordResult call() throws Exception {
		PasswordResult result = new PasswordResult();
		result.isCracked(false);
		
		CountDownLatch lock = new CountDownLatch(this.threadsForEachPassword);
			
		ArrayList<Thread> threads = new ArrayList<Thread>();
		int increment = 1;
		for(int i = 1; i <= this.threadsForEachPassword; i++) {
			SearchPartOfDictionaryTask task = new SearchPartOfDictionaryTask(this.dictionary.getAbsolutePath(),password,increment,increment + (this.totalWordsInDictionary/this.threadsForEachPassword),lock);
			task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {

					if(task.getValue() != null) {
						result.isCracked(true);
						result.setPlainTextPassword(task.getValue());
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								if(lv_CrackedPasswords.getItems().size() > 1000) {
									lv_CrackedPasswords.getItems().clear();
								}
								 lv_CrackedPasswords.getItems().add(result.getPlainTextPassword());
								 lbl_TotalCracked.setText(Integer.toString(Integer.parseInt(lbl_TotalCracked.getText())+1));
								
								 if(result.getPasswordStrength() == 0) {
									 lbl_s0.setText(Integer.toString(Integer.parseInt(lbl_s0.getText()) + 1));
									 
								 }
								 else if(result.getPasswordStrength() == 1) {
									 lbl_s1.setText(Integer.toString(Integer.parseInt(lbl_s1.getText()) + 1));
								 }
								 else if(result.getPasswordStrength() == 2) {
									 lbl_s2.setText(Integer.toString(Integer.parseInt(lbl_s2.getText()) + 1));
								 } else if(result.getPasswordStrength() == 3) {
									 lbl_s3.setText(Integer.toString(Integer.parseInt(lbl_s3.getText()) + 1));
								 }
								 else if(result.getPasswordStrength() == 4) {
									 lbl_s4.setText(Integer.toString(Integer.parseInt(lbl_s4.getText()) + 1));
								 }
								 
								 /*
								  * 
								  * 
								  * passwordStatLabels[0] = lblTotalBeginningWithUppercase;
		passwordStatLabels[1] = lblTotalBeginningWithLowercase;
		passwordStatLabels[2] = lblTotalBeginningWithNumber;
		
		passwordStatLabels[3] = lblTotalBeginningWithLetter;
		
		passwordStatLabels[4] = lblTotalEndingWithLetter;
		passwordStatLabels[5] = lblTotalEndingWithNumber;
		
		passwordStatLabels[6] = lblTotalWithStringNumberCombination;
		passwordStatLabels[7] = lblAverageLettersPerWord;
		passwordStatLabels[8] = lblAverageNumbersPerWord;
								  */
								 
								 if(result.BeginsWithUppercaseLetter()) {
									 passwordStatLabels[0].setText(Integer.toString(Integer.parseInt(passwordStatLabels[0].getText()) + 1));
								 }
								 if(result.BeginsWithLowercaseLetter()) {
									 passwordStatLabels[1].setText(Integer.toString(Integer.parseInt(passwordStatLabels[1].getText()) + 1));

								 }
								 if(result.BeginsWithNumber()) {
									 passwordStatLabels[2].setText(Integer.toString(Integer.parseInt(passwordStatLabels[2].getText()) + 1));

								 }
								 if(result.isStartsWithLetter()) {
									 passwordStatLabels[3].setText(Integer.toString(Integer.parseInt(passwordStatLabels[3].getText()) + 1));

								 }
								 if(result.EndsWithLetter()) {
									 passwordStatLabels[4].setText(Integer.toString(Integer.parseInt(passwordStatLabels[4].getText()) + 1));

								 }
								 if(result.endsWithNumber()) {
									 passwordStatLabels[5].setText(Integer.toString(Integer.parseInt(passwordStatLabels[5].getText()) + 1));

								 }
								 if(result.isStringNumberCombination()) {
									 passwordStatLabels[6].setText(Integer.toString(Integer.parseInt(passwordStatLabels[6].getText()) + 1));

								 }
								 if(result.ContainsAllLetters()) {
									 passwordStatLabels[7].setText(Integer.toString(Integer.parseInt(passwordStatLabels[7].getText()) + 1));

								 }
								 if(result.ContainsAllNumbers()) {
									 passwordStatLabels[8].setText(Integer.toString(Integer.parseInt(passwordStatLabels[8].getText()) + 1));

								 }
								 if(result.isContainsAllTheSameCharacters()) {
									 passwordStatLabels[9].setText(Integer.toString(Integer.parseInt(passwordStatLabels[9].getText()) + 1));

								 }
								 if(result.isContains3OrMoreRepeatingCharacters()) {
									 passwordStatLabels[10].setText(Integer.toString(Integer.parseInt(passwordStatLabels[10].getText()) + 1));

								 }
								 if(result.isContains5OrMoreRepeatingCharacters()) {
									 passwordStatLabels[11].setText(Integer.toString(Integer.parseInt(passwordStatLabels[11].getText()) + 1));

								 }
								 if(result.isContains7OrMoreRepeatingCharacters()) {
									 passwordStatLabels[12].setText(Integer.toString(Integer.parseInt(passwordStatLabels[12].getText()) + 1));

								 }
								 
			
							}
							
						});
					
					}
					
				}
			});
			threads.add(new Thread(task));
			increment += (this.totalWordsInDictionary/this.threadsForEachPassword);
			
			
		}
	
		
		for(Thread th : threads) {
			th.start();
		}
		
		lock.await();
		
		for(Thread th : threads) {
			th.interrupt();
		}
		
		
				
		return result;
	}
	
	
}
