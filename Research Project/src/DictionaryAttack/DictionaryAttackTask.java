package DictionaryAttack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DictionaryAttackTask extends Task<DictionaryAttackResult> {
	private File dictionaryList;
	private File passwordList;
	private int dictionaryLength;
	private ListView lv_crackedPasswords;
	private Label lbl_TotalCracked;
	private Label lbl_TotalFailed;
	private int totalWordsInDictionary;

	public DictionaryAttackTask(File dictionaryList, File passwordList, int dictionaryLength,
			
	    ListView lv_crackedPasswords, Label lblTotalCracked, Label lblTotalFailed, int totalWordsInDictionary) {

		this.dictionaryList = dictionaryList;
		this.passwordList = passwordList;
		this.dictionaryLength = dictionaryLength;
		this.lv_crackedPasswords = lv_crackedPasswords;
		this.lbl_TotalCracked = lblTotalCracked;
		this.lbl_TotalFailed = lblTotalFailed;
		this.totalWordsInDictionary = totalWordsInDictionary;
	}

	@Override
	protected DictionaryAttackResult call() throws Exception {
		System.out.println(this.totalWordsInDictionary);

		DictionaryAttackResult attackResult = new DictionaryAttackResult();

		BufferedReader reader = new BufferedReader(new FileReader(passwordList.getAbsolutePath()));

		String line = reader.readLine();
		int totalPasswords = 0;
		ArrayList<Task> totalTasks = new ArrayList<Task>();
		 while (line != null) {
			 
		    CountDownLatch latch = new CountDownLatch(10);
			 
			 for(int i = 1; i <=10; i++) {
				 
					PasswordSearchTask searchTask = new PasswordSearchTask(latch, new String(line), this.dictionaryList,
							this.lv_crackedPasswords, this.totalWordsInDictionary);
					searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent t) {
							attackResult.incrementTotalWordsChecked();
							Platform.runLater(new Runnable() {

								@Override
								public void run() {
									if (searchTask.getValue().isCracked) {
										lbl_TotalCracked
												.setText(Integer.toString(Integer.parseInt(lbl_TotalCracked.getText()) + 1));
									} else {
										lbl_TotalFailed
												.setText(Integer.toString(Integer.parseInt(lbl_TotalFailed.getText()) + 1));
									}

								}

							});
							updateProgress(attackResult.getTotalWordsChecked(), 1000);

							// System.out.println(searchTask.getValue());
						}
					});
				 
				 totalTasks.add(searchTask);
				 totalPasswords++;
				 line = reader.readLine();
				 if(line == null) {
					 int latchSize = 10;
					 while(latchSize > totalTasks.size()) {
						 latch.countDown();
						 latchSize--;
					 }
					 break;
				 }
				 
			 }
			 

			 totalPasswords = 0;

			for(Task t : totalTasks) {
				Thread thread = new Thread(t);
				
				thread.start();
				thread.sleep(new Random().nextInt(5000));
			}

				
				latch.await();
				totalTasks.clear();
 				//line = reader.readLine();

			}
		
		
		/*
		 * while (line != null) {
			CountDownLatch latch = new CountDownLatch(1);

			PasswordSearchTask searchTask = new PasswordSearchTask(latch, new String(line), this.dictionaryList,
					this.lv_crackedPasswords, this.totalWordsInDictionary);
			searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent t) {
					attackResult.incrementTotalWordsChecked();
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							if (searchTask.getValue().isCracked) {
								lbl_TotalCracked
										.setText(Integer.toString(Integer.parseInt(lbl_TotalCracked.getText()) + 1));
							} else {
								lbl_TotalFailed
										.setText(Integer.toString(Integer.parseInt(lbl_TotalFailed.getText()) + 1));
							}

						}

					});
					updateProgress(attackResult.getTotalWordsChecked(), 500000000);

					// System.out.println(searchTask.getValue());
				}
			});

			Thread t = new Thread(searchTask);
			t.start();
			latch.await();
			line = reader.readLine();

		}

		 */

		/*
		 * Task<Integer> task = new Task<Integer>() {
		 * 
		 * @Override protected Integer call() throws Exception { int iterations; for
		 * (iterations = 0; iterations < 10000; iterations++) { if (isCancelled()) {
		 * break; } } return iterations; } }; Callable<Integer> callable = () -> {
		 * task.run(); return task.getValue(); }; Task<Integer> task2 = new
		 * Task<Integer>() {
		 * 
		 * @Override protected Integer call() throws Exception { int iterations; for
		 * (iterations = 0; iterations < 10000; iterations++) { if (isCancelled()) {
		 * break; } } return iterations; } };
		 * 
		 * task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new
		 * EventHandler<WorkerStateEvent>() {
		 * 
		 * @Override public void handle(WorkerStateEvent t) { updateProgress(2000,
		 * 3000); attackResult.incrementTotalWordsChecked(); } });
		 * task2.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new
		 * EventHandler<WorkerStateEvent>() {
		 * 
		 * @Override public void handle(WorkerStateEvent t) { updateProgress(3000,
		 * 3000); attackResult.incrementTotalWordsChecked(); } });
		 * 
		 * Callable<Integer> callable2 = () -> { task2.run(); return task2.getValue();
		 * };
		 * 
		 * 
		 * ExecutorService executor = Executors.newFixedThreadPool(2);
		 * 
		 * /*List<Callable<Integer>> taskList2 = new ArrayList<Callable<Integer>>();
		 * taskList.add(callable); taskList.add(callable2); try {
		 * executor.invokeAll(taskList);
		 * 
		 * } catch (Exception e) {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * int result = task.get(); int result2 = task2.get();
		 * 
		 * executor.shutdown(); System.out.print("result: " + result + "   Result 2: " +
		 * result2);
		 */

		return attackResult;

	}

}
