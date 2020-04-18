package DictionaryAttack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.nulabinc.zxcvbn.Zxcvbn;
import com.nulabinc.zxcvbn.matchers.Match;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import passwordListGeneration.SHA1HashGenerator;

public class DictionaryAttackTask extends Task<DictionaryAttackResult> {
	private File dictionaryList;
	private File passwordList;
	private ListView lv_crackedPasswords;
	private Label lbl_TotalCracked;
	private Label lbl_TotalFailed;
	private int totalWordsInDictionary;
	private int totalWordsToCheckAtOnce;
	private PieChart chart_WordLengthDistribution;
	private int totalThreadsForEachPassword;
	public ReentrantLock lock = new ReentrantLock();
	public CountDownLatch latch1 = new CountDownLatch(1);
	int totalChecked = 0;
	private Label s0;
	private Label s1;
	private Label s2;
	private Label s3;
	private Label s4;
	private File selectedOutputFile;
	private Label[] passwordStatLabels;
	private boolean addPasswordFeedback;
	private String attackName;
	private String attackDescription;

	public DictionaryAttackTask(String attackName, String attackDescription, boolean addPasswordFeedback, File dictionaryList, File passwordList,

			ListView lv_crackedPasswords, Label lblTotalCracked, Label lblTotalFailed, int totalWordsInDictionary,
			int totalWordstoCheckAtOnce, PieChart wordLengthDistribution, int threadsForTotalPassword, Label strength0,
			Label strength1, Label strength2, Label strength3, Label strength4, File selectedOutputFile,
			Label[] passwordStatLabels) {

		this.attackDescription = attackDescription;
		this.attackName = attackName;
		this.addPasswordFeedback = addPasswordFeedback;
		this.passwordStatLabels = passwordStatLabels;
		this.totalWordsToCheckAtOnce = totalWordstoCheckAtOnce;
		this.dictionaryList = dictionaryList;
		this.passwordList = passwordList;
		this.selectedOutputFile = selectedOutputFile;
		this.lv_crackedPasswords = lv_crackedPasswords;
		this.lbl_TotalCracked = lblTotalCracked;
		this.lbl_TotalFailed = lblTotalFailed;
		this.totalWordsInDictionary = totalWordsInDictionary;
		this.chart_WordLengthDistribution = wordLengthDistribution;
		this.totalThreadsForEachPassword = threadsForTotalPassword;
		this.s0 = strength0;
		this.s1 = strength1;
		this.s2 = strength2;
		this.s3 = strength3;
		this.s4 = strength4;
	}

	public void configurePieChart(DictionaryAttackResult result) {

		this.chart_WordLengthDistribution.setAnimated(false);
		this.chart_WordLengthDistribution.setTitle("Lengths Of Cracked Passwords");

		this.chart_WordLengthDistribution.getData().clear();
		ObservableList<PieChart.Data> pieSlices = FXCollections.observableArrayList();

		this.chart_WordLengthDistribution.setLegendSide(Side.LEFT);

		HashMap<Integer, Integer> wordLengthDistribution = result.getWordLengthDistribution();

		for (Map.Entry mapElement : wordLengthDistribution.entrySet()) {

			PieChart.Data slice = new PieChart.Data(
					"Length: " + mapElement.getKey().toString() + "  Total: " + mapElement.getValue(),
					(Integer) mapElement.getValue());

			chart_WordLengthDistribution.getData().add(slice);

		}

		chart_WordLengthDistribution.getData().forEach(data -> {
			String lengthResult = Double.toString((data.getPieValue()));
			Tooltip toolTip = new Tooltip(lengthResult);
			Tooltip.install(data.getNode(), toolTip);
		});
	}

	@Override
	protected DictionaryAttackResult call() throws Exception {

		DictionaryAttackResult attackResult = new DictionaryAttackResult();

		int passwordListSize = getPasswordListSize();

		BufferedWriter crackedPasswordWriter = new BufferedWriter(new FileWriter(this.selectedOutputFile));
		crackedPasswordWriter.write("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Cracked Passwords>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		crackedPasswordWriter.newLine();
		crackedPasswordWriter.write("Attack Name: " + this.attackName);
		crackedPasswordWriter.newLine();
		crackedPasswordWriter.write("Attack Description: " + this.attackDescription);
		crackedPasswordWriter.newLine();

		BufferedReader reader = new BufferedReader(new FileReader(passwordList.getAbsolutePath()));

		String line = reader.readLine();

		int totalPasswords = 0;
		ArrayList<Task> totalTasks = new ArrayList<Task>();
		Vector<PasswordResult> passwordResults = new Vector<PasswordResult>();
		while (line != null) {

			CountDownLatch latch = new CountDownLatch(this.totalWordsToCheckAtOnce);
			for (int i = 1; i <= totalWordsToCheckAtOnce; i++) {

				PasswordSearchTask searchTask = new PasswordSearchTask(latch, new String(line), this.dictionaryList,
						this.lv_crackedPasswords, this.totalWordsInDictionary, this.totalThreadsForEachPassword,
						this.lbl_TotalCracked, this.lbl_TotalFailed, s0, s1, s2, s3, s4, this.passwordStatLabels);
				searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						new EventHandler<WorkerStateEvent>() {
							@Override
							public void handle(WorkerStateEvent t) {

								passwordResults.add(searchTask.getValue());

								Platform.runLater(new Runnable() {

									@Override
									public void run() {

										if(passwordListSize > 1000) {
											if (attackResult.getTotalWordsChecked() % 10 == 0) {
												configurePieChart(attackResult);
											}
										}else {
											configurePieChart(attackResult);
										}
										

										if (!searchTask.getValue().isCracked()) {
											lbl_TotalFailed.setText(
													Integer.toString(Integer.parseInt(lbl_TotalFailed.getText()) + 1));
										}

										/*
										 * if (searchTask.getValue().isCracked()) { lock.lock();
										 * lbl_TotalCracked.setText(Integer.toString(attackResult.getTotalCracked()));
										 * lock.unlock(); } else { lock.lock();
										 * lbl_TotalFailed.setText(Integer.toString(attackResult.getTotalWordsChecked()
										 * - attackResult.getTotalCracked())); lock.unlock(); }
										 */
									}

								});

								lock.lock();
								totalChecked = totalChecked + 1;
								lock.unlock();
								updateProgress(totalChecked, passwordListSize);

							}
						});

				totalTasks.add(searchTask);
				totalPasswords++;
				line = reader.readLine();
				if (line == null) {
					int latchSize = totalWordsToCheckAtOnce;
					while (latchSize > totalTasks.size()) {
						latch.countDown();
						latchSize--;
					}
					break;
				}

			}

			totalPasswords = 0;

			for (Task t : totalTasks) {
				Thread thread = new Thread(t);
				thread.start();
			}

			latch.await();
			
			if(passwordListSize < 1000) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						
						configurePieChart(attackResult);
						
					}
					
				});
				
			}
				

			for (PasswordResult passwordResult : passwordResults) {
				attackResult.AddPasswordResult(passwordResult);
				try {
					if (addPasswordFeedback && passwordResult.isCracked()) {

						crackedPasswordWriter.write(StringUtils.rightPad(passwordResult.getPlainTextPassword(), 20)
								+ StringUtils.rightPad("Strength:" + passwordResult.getPasswordStrength(), 15)
								+ StringUtils.rightPad("Patterns:" + passwordResult.getPatternIdentified(), 50)
								+ StringUtils.rightPad("Lists:" + passwordResult.getDictionaryListFound(), 50)
								+ " Password feedback: " + passwordResult.getPasswordFeedback());
						crackedPasswordWriter.newLine();

					} else if (!addPasswordFeedback && passwordResult.isCracked()) {
						crackedPasswordWriter.write(passwordResult.getPlainTextPassword());
						crackedPasswordWriter.newLine();
					}

				} catch (Exception e) {
					System.out.println("ERROR " + e.getLocalizedMessage());
				}
			}

			passwordResults.clear();

			totalTasks.clear();

			// line = reader.readLine();

		}

		/*
		 * while (line != null) { CountDownLatch latch = new CountDownLatch(1);
		 * 
		 * PasswordSearchTask searchTask = new PasswordSearchTask(latch, new
		 * String(line), this.dictionaryList, this.lv_crackedPasswords,
		 * this.totalWordsInDictionary);
		 * searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new
		 * EventHandler<WorkerStateEvent>() {
		 * 
		 * @Override public void handle(WorkerStateEvent t) {
		 * attackResult.incrementTotalWordsChecked(); Platform.runLater(new Runnable() {
		 * 
		 * @Override public void run() { if (searchTask.getValue().isCracked) {
		 * lbl_TotalCracked
		 * .setText(Integer.toString(Integer.parseInt(lbl_TotalCracked.getText()) + 1));
		 * } else { lbl_TotalFailed
		 * .setText(Integer.toString(Integer.parseInt(lbl_TotalFailed.getText()) + 1));
		 * }
		 * 
		 * }
		 * 
		 * }); updateProgress(attackResult.getTotalWordsChecked(), 500000000);
		 * 
		 * // System.out.println(searchTask.getValue()); } });
		 * 
		 * Thread t = new Thread(searchTask); t.start(); latch.await(); line =
		 * reader.readLine();
		 * 
		 * }
		 * 
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

		crackedPasswordWriter.close();
		return attackResult;

	}

	public int getPasswordListSize() throws IOException {

		Stream<String> s;
		try (Stream<String> all_lines = Files.lines(Paths.get(passwordList.getAbsolutePath()),
				StandardCharsets.ISO_8859_1)) {

			s = all_lines.skip(0);
			return (int) s.count();

		}

	}

}
