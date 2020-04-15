package application;
import javafx.scene.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import java.io.File;
import DictionaryAttack.DictionaryAttackTask;
import DictionaryListPaths.DictionaryList;
import javafx.beans.binding.When;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

public class DictionaryAttackController {
	
	@FXML private ProgressBar pgb_OverallProgress;
	@FXML Label lbl_TotalChecked;
	@FXML Label lbl_TotalCracked;
	@FXML Label lbl_TotalFailed;
	@FXML ListView listView_CrackedPasswords;
	private int totalWordsInDictionary;
	private int totalWordsToCheckAtOnce;
	private DictionaryList dictionaryList;
	private File selectedPasswordList;
	@FXML private PieChart chart_WordLengthDistribution;
	@FXML private Label lbl_TotalProgress;
	
	
	
	
	@FXML public void initialize() {
		
		

		
	}
	
	public void beginAttack() {

		DictionaryAttackTask task = new DictionaryAttackTask(new File(dictionaryList.getFilePath()), selectedPasswordList, listView_CrackedPasswords, this.lbl_TotalCracked, this.lbl_TotalFailed, this.getTotalWordsInDictionary(),this.totalWordsToCheckAtOnce,chart_WordLengthDistribution);
		Thread t = new Thread(task);
		pgb_OverallProgress.progressProperty().bind(task.progressProperty());
		lbl_TotalChecked.textProperty().bind(task.workDoneProperty().asString());
		lbl_TotalProgress.textProperty().bind(new When(task.progressProperty().isEqualTo(-1)).then("none").otherwise(task.progressProperty().multiply(100).asString("%.0f%%")));
		
		t.start();
		
		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				System.out.println("TASK Succeeded: " + task.getValue().getTotalWordsChecked());
			}
		});
				
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
	

}
