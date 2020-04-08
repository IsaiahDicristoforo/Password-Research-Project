package DictionaryAttack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;

public class PasswordSearchTask extends Task<PasswordResult> {


	private String password;
	private CountDownLatch countDownLatch;
	private File dictionary;
	private ListView lv_CrackedPasswords;
	private int totalWordsInDictionary;
	
	public PasswordSearchTask(CountDownLatch countDownLatch,String hashedPassword, File dictionary, ListView lv_CrackedPasswords, int totalWordsInDictionary) {
		password = new String(hashedPassword);
		this.dictionary = dictionary;
		this.totalWordsInDictionary = totalWordsInDictionary;
		this.lv_CrackedPasswords = lv_CrackedPasswords;
		this.countDownLatch = countDownLatch;
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
		result.isCracked = false;
		
		CountDownLatch lock = new CountDownLatch(10);
			
		ArrayList<Thread> threads = new ArrayList<Thread>();
		int increment = 1;
		for(int i = 1; i <= 10; i++) {
			SearchPartOfDictionaryTask task = new SearchPartOfDictionaryTask(this.dictionary.getAbsolutePath(),password,increment,increment + (this.totalWordsInDictionary/10),lock);
			task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					
					if(task.getValue() != null) {
						result.isCracked = true;
						result.setPlainTextPassword(task.getValue());
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								if(lv_CrackedPasswords.getItems().size() > 100) {
									lv_CrackedPasswords.getItems().clear();
								}
								 lv_CrackedPasswords.getItems().add(result.getPlainTextPassword());
								
							}
							
						});
					
					}
					
				}
			});
			threads.add(new Thread(task));
			increment += (this.totalWordsInDictionary/10);
			
			
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
