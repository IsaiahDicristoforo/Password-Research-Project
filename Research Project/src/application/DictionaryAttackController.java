package application;

import javafx.scene.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import com.nulabinc.zxcvbn.matchers.Match;

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
	
	public String attackName= "";
	public String attackDescription = "";
	
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
	@FXML private Label lblContainsAllSameCharacters;
	@FXML private Label lbl3OrMoreRepeating;
	@FXML private Label lbl5OrMoreRepeating;
	@FXML private Label lbl7OrMoreRepeating;
	
	@FXML private Label lblEstimatedCrackTime;
	@FXML Label lbl_DictionaryLists;
	@FXML Label lbl_PatternsIdentified;
	
	private boolean addFeedbackToPasswordList;

	
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
		        	try {
		        		Strength passwordStrength = new Zxcvbn().measure(listView_CrackedPasswords.getSelectionModel().getSelectedItem().toString());
			        	strength.setText(Integer.toString(passwordStrength.getScore()));
			        	lblEstimatedCrackTime.setText(passwordStrength.getCrackTimesDisplay().getOfflineSlowHashing1e4perSecond());


			        	lbl_DictionaryLists.setText("");
			        
			        	lbl_PatternsIdentified.setText("");
			        	ArrayList<String> patternNames = new ArrayList<String>();
						ArrayList<String> dictionaryNames = new ArrayList<String>();
						for(Match m : passwordStrength.getSequence()) {
						
							if(m.pattern.name() != null && !patternNames.contains(m.pattern.name())) {
								lbl_PatternsIdentified.setText(lbl_PatternsIdentified.getText() + " " + m.pattern.name());
							}
							
							if(m.dictionaryName != null && !dictionaryNames.contains(m.dictionaryName)) {
								lbl_DictionaryLists.setText(lbl_DictionaryLists.getText() + " " + m.dictionaryName);
							}
							
							patternNames.add(m.pattern.name());
							dictionaryNames.add(m.dictionaryName);
			        	}
			        	
			        	

		        	}catch(Exception e) {
		        		System.out.println("Error on list view index changed: "+ e.getLocalizedMessage());
		        	}
		        	
		        }
		    });
		 

	}

	public void beginAttack() {
		Label[] passwordStatLabels = new Label[13];
		passwordStatLabels[0] = lblTotalBeginningWithUppercase;
		passwordStatLabels[1] = lblTotalBeginningWithLowercase;
		passwordStatLabels[2] = lblTotalBeginningWithNumber;
		passwordStatLabels[3] = lblTotalBeginningWithLetter;
		passwordStatLabels[4] = lblTotalEndingWithLetter;
		passwordStatLabels[5] = lblTotalEndingWithNumber;
		passwordStatLabels[6] = lblTotalWithStringNumberCombination;
		passwordStatLabels[7] = lblAverageLettersPerWord;
		passwordStatLabels[8] = lblAverageNumbersPerWord;
		passwordStatLabels[9] = lblContainsAllSameCharacters;
		passwordStatLabels[10] = lbl3OrMoreRepeating;
		passwordStatLabels[11] = lbl5OrMoreRepeating;
		passwordStatLabels[12] = lbl7OrMoreRepeating;

		


		Task<Long> timeTask = new Task<Long>() {
			@Override
			protected Long call() throws Exception {

				StopWatch watch = new StopWatch();
				watch.start();
				long seconds;
				while (true) {

					seconds = watch.getTime(TimeUnit.SECONDS);
					updateMessage(Long.toString(seconds / 60) + " minutes " + Long.toString(seconds % 60) + " sec.");

					try {
						Thread.sleep(1000);
					} catch (InterruptedException interrupted) {
						
						if (isCancelled()) {
							break;
						}
					}

				}

				watch.stop();
				return watch.getTime();
			}
		};

		DictionaryAttackTask task = new DictionaryAttackTask(this.attackName, this.attackDescription,this.addFeedbackToPasswordList,new File(dictionaryList.getFilePath()),
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
		timeThread.start();
		t.start();

		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				timeTask.cancel();
				System.out.println("TASK Succeeded.  Total Words Checked " + task.getValue().getTotalWordsChecked());
				
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

	public boolean isAddFeedbackToPasswordList() {
		return addFeedbackToPasswordList;
	}

	public void setAddFeedbackToPasswordList(boolean addFeedbackToPasswordList) {
		this.addFeedbackToPasswordList = addFeedbackToPasswordList;
	}

}
