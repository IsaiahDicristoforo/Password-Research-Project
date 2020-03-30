package application;

import java.util.HashMap;
import java.util.Map;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import listAnalysis.DictionaryAnalysisResult;

public class DictionaryAnalysisResultsController {
	
	private DictionaryAnalysisResult testResult;
	
	@FXML private PieChart pc_WordDistribution;
	@FXML private PieChart pc_ContainsSpecialCharacters;
	@FXML private Label wordsStartingWithLetter;
	@FXML private Label wordsStartingWithNonNumber;
	@FXML private Label fileName;
	@FXML private Label totalWords;
	
	@FXML
	public void initialize() {

	}
	
	public void ConfigureResultsInfo() {
		fileName.setText(testResult.getFileName());
		totalWords.setText(Integer.toString(testResult.getTotalWords()));
		wordsStartingWithLetter.setText(Integer.toString(testResult.getTotalWordsBeginningWithLetter()));
		wordsStartingWithNonNumber.setText(Integer.toString(testResult.getTotalWordsStartingWithNonLetter()));

		pc_ContainsSpecialCharacters.setLegendVisible(true);
		pc_ContainsSpecialCharacters.getData().add(new PieChart.Data("Words Containing -!- " +  testResult.getTotalWordsWithExclamationPoint() , (Integer) testResult.getTotalWordsWithExclamationPoint()));
		pc_ContainsSpecialCharacters.getData().add(new PieChart.Data("Words Containing -#- " + testResult.getTotalWordsWithPoundSign(), (Integer) testResult.getTotalWordsWithPoundSign()));
		pc_ContainsSpecialCharacters.getData().add(new PieChart.Data("Words Containing -@- " + (testResult.getTotalWordsWithAtSign()), (Integer) testResult.getTotalWordsWithAtSign()));

	}
	
	public void ConfigureBarGraph() {
		
		ObservableList<PieChart.Data> pieSlices = FXCollections.observableArrayList();
		
		HashMap<Integer, Integer> wordLengthDistribution = testResult.getWordLengths();
		

		for(Map.Entry mapElement : wordLengthDistribution.entrySet()) {
			
			PieChart.Data slice = new PieChart.Data("Length: " + mapElement.getKey().toString() , (Integer) mapElement.getValue());
			
			pc_WordDistribution.getData().add(slice);

		}
		
		pc_WordDistribution.getData().forEach(data -> {
			String result = Double.toString((data.getPieValue()));
		    Tooltip toolTip = new Tooltip(result);
		    Tooltip.install(data.getNode(), toolTip);
		});
		

		
	}

	public DictionaryAnalysisResult getTestResult() {
		return testResult;
	}

	public void setTestResult(DictionaryAnalysisResult testResult) {
		this.testResult = testResult;
	}

}
