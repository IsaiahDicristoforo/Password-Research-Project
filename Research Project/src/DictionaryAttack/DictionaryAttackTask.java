package DictionaryAttack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

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

	public DictionaryAttackTask(File dictionaryList, File passwordList,

			ListView lv_crackedPasswords, Label lblTotalCracked, Label lblTotalFailed, int totalWordsInDictionary,
			int totalWordstoCheckAtOnce, PieChart wordLengthDistribution) {

		this.totalWordsToCheckAtOnce = totalWordstoCheckAtOnce;
		this.dictionaryList = dictionaryList;
		this.passwordList = passwordList;
		this.lv_crackedPasswords = lv_crackedPasswords;
		this.lbl_TotalCracked = lblTotalCracked;
		this.lbl_TotalFailed = lblTotalFailed;
		this.totalWordsInDictionary = totalWordsInDictionary;
		this.chart_WordLengthDistribution = wordLengthDistribution;
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
					"Length: " + mapElement.getKey().toString() + "  Total Cracked Passwords: " + mapElement.getValue(),
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
		
		BufferedReader reader = new BufferedReader(new FileReader(passwordList.getAbsolutePath()));

		String line = reader.readLine();
		int totalPasswords = 0;
		ArrayList<Task> totalTasks = new ArrayList<Task>();
		while (line != null) {

			CountDownLatch latch = new CountDownLatch(this.totalWordsToCheckAtOnce);
			for (int i = 1; i <= totalWordsToCheckAtOnce; i++) {

				PasswordSearchTask searchTask = new PasswordSearchTask(latch, new String(line), this.dictionaryList,
						this.lv_crackedPasswords, this.totalWordsInDictionary);
				searchTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
						new EventHandler<WorkerStateEvent>() {
							@Override
							public void handle(WorkerStateEvent t) {
								attackResult.AddPasswordResult(searchTask.getValue());
								Platform.runLater(new Runnable() {

									@Override
									public void run() {

										if (attackResult.getTotalWordsChecked() % 10 == 0) {
											configurePieChart(attackResult);
										}

										if (searchTask.getValue().isCracked()) {
											lbl_TotalCracked.setText(
													Integer.toString(Integer.parseInt(lbl_TotalCracked.getText()) + 1));
										} else {
											lbl_TotalFailed.setText(
													Integer.toString(Integer.parseInt(lbl_TotalFailed.getText()) + 1));
										}
									}

								});
								updateProgress(attackResult.getTotalWordsChecked(), passwordListSize);

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
				Thread.sleep(new Random().nextInt(200));
				thread.start();
			}

			latch.await();
			if (latch.getCount() == 0) {
				totalTasks.clear();
			}

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
