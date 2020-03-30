package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import DictionaryListPaths.DictionaryList;
import DictionaryListPaths.ListTracker;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class DictionarySelectorController {

	@FXML
	private Button btn_AddYourOwndictionary;

	@FXML
	private ListView<DictionaryList> DictionaryPathList;
	
	@FXML 
	private Button RemoveAllButton;
	
	private DictionaryList SelectedList;
	
	@FXML private Button btn_submit;
	


	@FXML
	public void initialize() {

		FillListBoxFromFile();

	}
	
	public void OnSubmit() {
		
		if(!DictionaryPathList.getSelectionModel().isEmpty()) {
			Stage currentStage = (Stage)btn_AddYourOwndictionary.getScene().getWindow();
			currentStage.close();
			SelectedList = DictionaryPathList.getSelectionModel().getSelectedItem();
		}
		
		
	}
	
	public DictionaryList GetSelectedList() {
		return SelectedList;
	}
	
	public void RemoveEntries() {
		
	DictionaryPathList.getItems().clear();
		
	//This clears the file containing the serialized object
		try {
			FileOutputStream fileOut = new FileOutputStream(new File("DictionaryLists.ser"), false);
			fileOut.close();
			
		} catch (IOException i) {
			i.printStackTrace();
		}

		
	}

	private void FillListBoxFromFile() {
		
		ListTracker objectInFile  = null;
	      try {
	         FileInputStream fileIn = new FileInputStream("DictionaryLists.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         objectInFile = (ListTracker) in.readObject();
	         in.close();
	         fileIn.close();
	         
	         ArrayList<DictionaryList> items = objectInFile.getItems();
	        
	         for(int i = 0;  i < items.size(); i++) {
	        	 DictionaryPathList.getItems().add(items.get(i));
	         }
	         
	       //  FXCollections.reverse((DictionaryPathList.getItems()));
	         	         
	      } catch (IOException i) {
	    	  //An exception will be thrown if the serialized object is rmpty
	         return;
	      } catch (ClassNotFoundException c) {
	         c.printStackTrace();
	         return;
	      }
		
		
	}

	public void on_btn_AddYourOwndictionary(ActionEvent e) {
		OpenFileChooser();
	}

	private void OpenFileChooser() {

		boolean validFile = false;

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select A Dictionary.  Text Files Only!");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
		File selectedFile = fileChooser.showOpenDialog(btn_AddYourOwndictionary.getScene().getWindow());

		if ((selectedFile == null || !selectedFile.getPath().endsWith(".txt"))) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Error");
			alert.setHeaderText("Incorrect File");
			alert.setContentText("Information");
			alert.showAndWait();
		} else {

			ListTracker listOptions = new ListTracker();
			
			
			DictionaryList listToAdd = new DictionaryList(selectedFile);

			listOptions.addDictionaryList(listToAdd);

			
			for(DictionaryList l : DictionaryPathList.getItems()) {
				listOptions.addDictionaryList(l);
			}
			
			
			DictionaryPathList.getItems().add(listToAdd);

			try {
				FileOutputStream fileOut = new FileOutputStream(new File("DictionaryLists.ser"), false);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(listOptions);
				out.close();
				fileOut.close();
			} catch (IOException i) {
				i.printStackTrace();
			}
		}
		
	

	}

}
