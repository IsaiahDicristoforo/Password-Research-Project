package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import DictionaryListPaths.DictionaryList;
import javafx.animation.FadeTransition;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import listAnalysis.DictionaryAnalysisResult;
import listAnalysis.DictionaryAnalysisTask;
import passwordListGeneration.AppendBehavior;
import passwordListGeneration.GeneratedPasswordList;
import passwordListGeneration.LettersNumbersAndSpecialCharactersAppender;
import passwordListGeneration.LettersNumbersAndSpecialCharactersPrepender;
import passwordListGeneration.ListItemGenerator;
import passwordListGeneration.LowercaseLetterAppender;
import passwordListGeneration.LowercaseLetterPrepender;
import passwordListGeneration.NoAppendBehavior;
import passwordListGeneration.NoPrependBehavior;
import passwordListGeneration.NumberAppender;
import passwordListGeneration.NumberPrepender;
import passwordListGeneration.PrependBehavior;
import passwordListGeneration.SHA1HashGenerator;
import passwordListGeneration.UppercaseLetterAppender;
import passwordListGeneration.UppercaseLetterPrepender;
import passwordListGeneration.WordSource;

public class ApplicationController {
	@FXML
	private Button btn_CreateNewDictionaryTest;
	@FXML
	private ScrollPane ScrollPane_MainTestPain;
	@FXML
	private Label lbl_DictionaryList;
	@FXML
	private Button btn_NewDictionaryList;
	@FXML
	private Button btn_RunDictionaryAnalysis;
	@FXML
	private Label lbl_DictionaryListPath;
	@FXML
	private ProgressBar progressBar_RunDictionaryAnalysis1;
	private DictionaryList list;
	@FXML
	private Label lbl_DictionaryAnalysisProgress;
	@FXML
	private Label lbl_TotalWordsCounter;
	@FXML
	private Pane pane_DictionaryAnalysisProgress;
	@FXML
	private TextField tf_TestName;
	@FXML
	private TextArea ta_TestInformation;
	@FXML
	private Accordion accordian_mainWindows;
	@FXML
	private Button btn_CreateNewBruteForceAttack;
	@FXML
	private Button btn_TestBoth;
	@FXML
	private Button btn_ViewDictionaryTestResults;
	private DictionaryAnalysisResult dictionaryAnalysisResult;
	@FXML private Button btn_SpecifyPasswordListGenerationLocation;
	@FXML private Button btn_AddWordsToList;
	@FXML private TextField textField_TotalPasswordsToAdd;
	@FXML private ChoiceBox<WordSource> choiceBox_PasswordSource;
	@FXML private ChoiceBox<AppendBehavior> choiceBox_AppendOptions;
	@FXML private TextField textField_TotalCharactersToAppend;
	@FXML private ChoiceBox<PrependBehavior> choiceBox_PrependOptions;
	@FXML private TextField textField_TotalCharactersToPrepend;
	@FXML private Label label_generatedListSize;
	private GeneratedPasswordList generatedPasswordList;
	@FXML private Label lbl_selectedPasswordList;
	@FXML private Button btn_AddHashedPasswordList;
	@FXML private Button btn_AddUnhashedPasswordList;
	private File selectedPasswordFile;
	@FXML private Button btn_beginTest;
	@FXML private TextField textField_TotalWordsInDicrionaryList;
	private int totalWordsInDicrionaryList;
	@FXML private TextField textField_TotalWordsToCheckAtOnce;
	
	
	
	public void beginDictionaryAttack() {
		
		Parent root;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Dictionary Attack.fxml"));
			root = loader.load();
			DictionaryAttackController controller = loader
					.<DictionaryAttackController>getController();
			
			controller.setDictionaryList(list);
			controller.setSelectedPasswordList(this.selectedPasswordFile);
			controller.setTotalWordsInDictionary(Integer.parseInt(this.textField_TotalWordsInDicrionaryList.getText()));
		
			controller.setTotalWordsToCheckAtOnce(Integer.parseInt(this.textField_TotalWordsToCheckAtOnce.getText()));
			controller.beginAttack();
			
			Scene scene = new Scene(root);

			Stage stage = new Stage();
			stage.setScene(scene);

			stage.showAndWait();

			
		}catch(Exception e) {
			e.printStackTrace();
		}

	
	}
	
	

	public boolean IsValidPasswordGeneration() {
		try {
			Integer.parseInt(this.textField_TotalPasswordsToAdd.getText());
			Integer.parseInt(this.textField_TotalCharactersToAppend.getText());			
			Integer.parseInt(this.textField_TotalCharactersToPrepend.getText());
			
		}catch(Exception e) {
			return false;
		}
		
		return true;		
	}
	
	public void configurePasswordGeneration() {
		this.textField_TotalPasswordsToAdd.clear();
		this.textField_TotalCharactersToAppend.clear();
		this.label_generatedListSize.setText("0");
		this.textField_TotalCharactersToPrepend.clear();
		if(this.choiceBox_PasswordSource.getSelectionModel().isEmpty()) {
			this.choiceBox_PasswordSource.getItems().add(WordSource.DictionaryWord);
			this.choiceBox_PasswordSource.getItems().add(WordSource.PersonName);
			this.choiceBox_PasswordSource.getItems().add(WordSource.RandomCharacters);	
		}
		if(this.choiceBox_AppendOptions.getSelectionModel().isEmpty()) {
			this.choiceBox_AppendOptions.getItems().add(new LettersNumbersAndSpecialCharactersAppender());
			this.choiceBox_AppendOptions.getItems().add(new LowercaseLetterAppender());
			this.choiceBox_AppendOptions.getItems().add(new NumberAppender());
			this.choiceBox_AppendOptions.getItems().add(new UppercaseLetterAppender());
			this.choiceBox_AppendOptions.getItems().add(new NoAppendBehavior());
		}
		if(this.choiceBox_PrependOptions.getItems().isEmpty()) {
			this.choiceBox_PrependOptions.getItems().add(new LettersNumbersAndSpecialCharactersPrepender());
			this.choiceBox_PrependOptions.getItems().add(new LowercaseLetterPrepender());
			this.choiceBox_PrependOptions.getItems().add(new NumberPrepender());
			this.choiceBox_PrependOptions.getItems().add(new UppercaseLetterPrepender());
			this.choiceBox_PrependOptions.getItems().add(new NoPrependBehavior());
		}
	}
	
	
	public void getPasswordOutput() {
		
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Choose a place to save your hashed passwordFile.");
		
		File selectedDirectory = chooser.showDialog(this.btn_AddWordsToList.getScene().getWindow());
		
		if(!(selectedDirectory == null)) {
			btn_AddWordsToList.setDisable(false);
			generatedPasswordList = new GeneratedPasswordList(selectedDirectory.getPath());
		
			selectedPasswordFile = new File(generatedPasswordList.getFilePath() + "/PasswordList.txt");
			lbl_selectedPasswordList.setText(selectedPasswordFile.getName() + "   " + selectedPasswordFile.getPath());


		}
	}
	
	public void SelectHashedFile() {
		FileChooser chooser = new FileChooser();
		File selection = chooser.showOpenDialog(this.btn_AddHashedPasswordList.getScene().getWindow());
	
		if(!(selection == null)) {
			selectedPasswordFile = selection;
			lbl_selectedPasswordList.setText(selection.getName() + "  " + selection.getAbsolutePath());
		}
	}
	
	public void SelectUnhashedFile() {
		
		FileChooser chooser = new FileChooser();
		File selectedFile = chooser.showOpenDialog(this.accordian_mainWindows.getScene().getWindow());
		
		if(!(selectedFile == null)) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
				
				File newFile = new File((selectedFile.getPath()+ " Hashed").replaceFirst(".txt","") + ".txt");
				newFile.createNewFile();
				BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
				
				selectedPasswordFile = newFile;
				lbl_selectedPasswordList.setText(selectedFile.getName() + "  " + selectedFile.getAbsolutePath());
					
				String line = reader.readLine();
				while(line != null) {
					
					writer.append(new SHA1HashGenerator().generateHash(line) + " " + line);

					writer.newLine();
					
					line = reader.readLine();
				}
				
				reader.close();
				writer.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		
	}
	
	
	public void AddGeneratedPasswordsToList(){
		if(IsValidPasswordGeneration()) {
			generatedPasswordList.addAll(new ListItemGenerator(Integer.parseInt(this.textField_TotalPasswordsToAdd.getText()),
					Integer.parseInt(this.textField_TotalCharactersToAppend.getText()),
					Integer.parseInt(this.textField_TotalCharactersToPrepend.getText()), this.choiceBox_AppendOptions.getSelectionModel().getSelectedItem(),
					this.choiceBox_PrependOptions.getSelectionModel().getSelectedItem(),this.choiceBox_PasswordSource.getSelectionModel().getSelectedItem()).GeneratePasswords());
		
			this.label_generatedListSize.setText(Integer.toString(generatedPasswordList.size()));
			generatedPasswordList.WriteListToFile();
			
		}else {
			Alert a = new Alert(AlertType.ERROR, "You must fill out all required fields before generating your list.  In addition, all fields must contain a valid value");
			a.showAndWait();
		}
		
	}
	
	@FXML
	public void initialize() {
		btn_AddWordsToList.setDisable(true);
		configurePasswordGeneration();
		ScrollPane_MainTestPain.setVisible(false);
		btn_CreateNewDictionaryTest.textProperty();
	}

	public void AnalyzeSelectedDictionary() {

		DictionaryAnalysisTask task = new DictionaryAnalysisTask(list.getFilePath());
		Thread taskThread = new Thread(task);
		progressBar_RunDictionaryAnalysis1.progressProperty().bind(task.progressProperty());
		lbl_DictionaryAnalysisProgress.textProperty().bind(new When(task.progressProperty().isEqualTo(-1))
				.then("Loading...").otherwise(task.progressProperty().multiply(100).asString("%.0f%% complete")));
		lbl_TotalWordsCounter.textProperty().bind(new When(task.workDoneProperty().isEqualTo(-1)).then("")
				.otherwise(task.workDoneProperty().asString("%.0f")));

		task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				dictionaryAnalysisResult = task.getValue();
				btn_ViewDictionaryTestResults.setDisable(false);
				textField_TotalWordsInDicrionaryList.setText(Integer.toString(dictionaryAnalysisResult.getTotalWords()));
			}
		});
		

		taskThread.start();

	}

	@FXML
	public void OnClick(ActionEvent e) {
		ConfigureNewAttack();
		ConfigureDictionaryAttack();
	}

	public void ConfigureNewAttack() {
		if (ScrollPane_MainTestPain.isVisible()) {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setTitle("Warning");
			a.setContentText("You have an unsaved test.  Creating a new one will delete your unsaved work");
			Optional<ButtonType> result = a.showAndWait();

			// https://stackoverflow.com/questions/43031602/how-to-set-a-method-to-a-javafx-alert-button
			if (result.isPresent()) {
				if (result.get() == ButtonType.OK) {

					ta_TestInformation.setText("");
					tf_TestName.setText("");
					pane_DictionaryAnalysisProgress.setVisible(false);
				}
			}

		}

	}

	public void LoadDictionaryResultsWindow() {

		Parent root;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DictionaryAnalysisResults.fxml"));
			root = loader.load();
			DictionaryAnalysisResultsController controller = loader
					.<DictionaryAnalysisResultsController>getController();

			controller.setTestResult(dictionaryAnalysisResult);

			controller.ConfigureResultsInfo();
			
			controller.ConfigureBarGraph();

			Scene scene = new Scene(root);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ConfigureDictionaryAttack() {

		FadeTransition ft = new FadeTransition(Duration.millis(1000), ScrollPane_MainTestPain);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		ScrollPane_MainTestPain.setVisible(true);
		accordian_mainWindows.getPanes().get(1).setVisible(false);
		accordian_mainWindows.getPanes().get(0).setVisible(true);
		accordian_mainWindows.getPanes().get(0).setExpanded(true);
		pane_DictionaryAnalysisProgress.setVisible(false);

	}

	public void ConfigureBruteForceAttack() {
		FadeTransition ft = new FadeTransition(Duration.millis(1000), ScrollPane_MainTestPain);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		ConfigureNewAttack();
		ScrollPane_MainTestPain.setVisible(true);
		accordian_mainWindows.getPanes().get(0).setVisible(false);
		accordian_mainWindows.getPanes().get(1).setExpanded(true);
		accordian_mainWindows.getPanes().get(1).setVisible(true);
	}

	public void ConfigureTestBothAttack() {
		FadeTransition ft = new FadeTransition(Duration.millis(1000), ScrollPane_MainTestPain);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		ConfigureNewAttack();
		accordian_mainWindows.getPanes().get(0).setVisible(true);
		accordian_mainWindows.getPanes().get(1).setVisible(true);
		accordian_mainWindows.getPanes().get(2).setVisible(true);
	}

	public void LoadNewDictionaryWindow() {
		btn_NewDictionaryList.setDisable(true);
		Parent root;
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("DictionarySelector.fxml"));
			root = loader.load();
			DictionarySelectorController controller = loader.<DictionarySelectorController>getController();

			Scene scene = new Scene(root);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.showAndWait();

			if(controller.GetSelectedList() != null) {
				list = controller.GetSelectedList();

				lbl_DictionaryList.setText(
						list.getFileName() + "   " + (String.format(Double.toString(list.getFileSize()), "%.2f")) + "mb");
				lbl_DictionaryList.setTextFill(Color.GREEN);

				lbl_DictionaryListPath.setText(list.getFilePath());

				pane_DictionaryAnalysisProgress.setVisible(true);
				btn_ViewDictionaryTestResults.setDisable(true);
			}
			btn_NewDictionaryList.setDisable(false);
			

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

}
