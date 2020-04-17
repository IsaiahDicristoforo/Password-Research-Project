package application;

import javafx.scene.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.nulabinc.zxcvbn.Zxcvbn;

import DictionaryAttack.DictionaryAttackTask;
import DictionaryListPaths.DictionaryList;
import javafx.beans.binding.When;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class DictionaryAttackController {

	@FXML
	private ProgressBar pgb_OverallProgress;
	@FXML
	Label lbl_TotalChecked;
	@FXML
	Label lbl_TotalCracked;
	@FXML
	Label lbl_TotalFailed;
	@FXML
	ListView listView_CrackedPasswords;
	private int totalWordsInDictionary;
	private int totalWordsToCheckAtOnce;
	private DictionaryList dictionaryList;
	private File selectedPasswordList;
	@FXML
	private PieChart chart_WordLengthDistribution;
	@FXML
	private Label lbl_TotalProgress;
	private int threadsForTotalPassword;
	@FXML
	private Label s0;
	@FXML
	private Label s1;
	@FXML
	private Label s2;
	@FXML
	private Label s3;
	@FXML
	private Label s4;
	@FXML
	private Label elaspedTime;
	@FXML private Label strength;
	
	@FXML private File selectedOutputFile;
	
	@FXML private Label lblTotalBeginningWithUppercase;
	@FXML private Label lblTotalBeginningWithLowercase;
	@FXML private Label lblTotalBeginningWithNumber;
	@FXML private Label lblTotalBeginningWithLetter;
	@FXML private Label lblTotalEndingWithLetter;
	@FXML private Label lblTotalEndingWithNumber;
	@FXML private Label lblTotalWithStringNumberCombination;
	@FXML private Label lblAverageLettersPerWord;
	@FXML private Label lblAverageNumbersPerWord;

	
	public File getSelectedOutputFile() {
		return selectedOutputFile;
	}

	public void setSelectedOutputFile(File selectedOutputFile) {
		this.selectedOutputFile = selectedOutputFile;
	}

	@FXML
	public void initialize() {
		
		 listView_CrackedPasswords.setOnMouseClicked(new EventHandler<MouseEvent>() {

		        @Override
		        public void handle(MouseEvent event) {
		        	
		        	strength.setText(Integer.toString(new Zxcvbn().measure(listView_CrackedPasswords.getSelectionModel().getSelectedItem().toString()).getScore()));
		        
		        }
		    });
		 

	}

	public void beginAttack() {
		Label[] passwordStatLabels = new Label[9];
		passwordStatLabels[0] = lblTotalBeginningWithUppercase;
		passwordStatLabels[1] = lblTotalBeginningWithLowercase;
		passwordStatLabels[2] = lblTotalBeginningWithNumber;
		passwordStatLabels[3] = lblTotalBeginningWithLetter;
		passwordStatLabels[4] = lblTotalEndingWithLetter;
		passwordStatLabels[5] = lblTotalEndingWithNumber;
		passwordStatLabels[6] = lblTotalWithStringNumberCombination;
		passwordStatLabels[7] = lblAverageLettersPerWord;
		passwordStatLabels[8] = lblAverageNumbersPerWord;




		Task<Long> timeTask = new Task<Long>() {
			@Override
			protected Long call() throws Exception {

				StopWatch watch = new StopWatch();
				watch.start();
				long seconds;
				while (true) {

					seconds = watch.getTime(TimeUnit.SECONDS);
					updateMessage(Long.toString(seconds / 60) + " minutes " + Long.toString(seconds % 60) + " seconds");

					try {
						Thread.sleep(1000);
					} catch (InterruptedException interrupted) {
						
						if (isCancelled()) {
							updateMessage("Cancelled");
							break;
						}
					}

				}

				watch.stop();
				return watch.getTime();
			}
		};

		DictionaryAttackTask task = new DictionaryAttackTask(new File(dictionaryList.getFilePath()),
				selectedPasswordList, listView_CrackedPasswords, this.lbl_TotalCracked, this.lbl_TotalFailed,
				this.getTotalWordsInDictionary(), this.totalWordsToCheckAtOnce, chart_WordLengthDistribution,
				this.threadsForTotalPassword, s0, s1, s2, s3, s4, this.selectedOutputFile, passwordStatLabels);

		Thread t = new Thread(task);
		pgb_OverallProgress.progressProperty().bind(task.progressProperty());
		lbl_TotalChecked.textProperty().bind(task.workDoneProperty().asString());
		lbl_TotalProgress.textProperty().bind(new When(task.progressProperty().isEqualTo(-1)).then("none")
				.otherwise(task.progressProperty().multiply(100).asString("%.0f%%")));
		elaspedTime.textProperty().bind(timeTask.messageProperty());
		
		Thread timeThread = new Thread(timeTask);
		timeTask.cancel();
		t.start();

		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				System.out.println("TASK Succeeded: " + task.getValue().getTotalWordsChecked());
				System.out.println(task.getValue().getTotalBeginningWithLowercaseLetter()
						+ " words begin with a lowercase letter");
				System.out.println(task.getValue().getTotalWordsWithStringNumberCombination() + " word # combination");
				System.out.println(task.getValue().getTotalBeginningWithUppercaseLetter()
						+ " words begin with an uppercase letter");
				timeThread.interrupt();

			}
		});

	}
	
	public void OnCrackedPasswordClicked() {
		
		
		
	}

	public DictionaryList getDictionaryList() {
		return dictionaryList;
	}

	public void setDictionaryList(DictionaryList dictionaryList) {
		this.dictionaryList = dictionaryList;
	}

	public File getSelectedPasswordList() {
		return selectedPasswordList;
	}

	public void setSelectedPasswordList(File selectedPasswordList) {
		this.selectedPasswordList = selectedPasswordList;
	}

	public int getTotalWordsInDictionary() {
		return totalWordsInDictionary;
	}

	public void setTotalWordsInDictionary(int totalWordsInDictionary) {
		this.totalWordsInDictionary = totalWordsInDictionary;
	}

	public int getTotalWordsToCheckAtOnce() {
		return totalWordsToCheckAtOnce;
	}

	public void setTotalWordsToCheckAtOnce(int totalWordsToCheckAtOnce) {
		this.totalWordsToCheckAtOnce = totalWordsToCheckAtOnce;
	}

	public int getThreadsForTotalPassword() {
		return threadsForTotalPassword;
	}

	public void setThreadsForTotalPassword(int threadsForTotalPassword) {
		this.threadsForTotalPassword = threadsForTotalPassword;
	}

}
