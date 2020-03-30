package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import DictionaryListPaths.DictionaryList;
import DictionaryListPaths.ListTracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DictionarySelectorController {

	@FXML private Button btn_AddYourOwndictionary;
	
	public String hello = "Hello";
	
 
	public void on_btn_AddYourOwndictionary(ActionEvent e) {
		OpenFileChooser();
	}


	private void OpenFileChooser() {
		
		
		boolean validFile = false;
		
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select A Dictionary.  Text Files Only!");
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fileChooser.showOpenDialog(btn_AddYourOwndictionary.getScene().getWindow());

			if((selectedFile == null || !selectedFile.getPath().endsWith(".txt"))){
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Error");
		        alert.setHeaderText("Incorrect File");
		        alert.setContentText("Information");
		        alert.showAndWait();
			}else {
				ListTracker listOptions = new ListTracker();
				
				
				listOptions.addDictionaryList(new DictionaryList(selectedFile));
				
				  try {
				         FileOutputStream fileOut =
				         new FileOutputStream(new File("DictionaryLists.ser"), false);

				         
				         ObjectOutputStream out = new ObjectOutputStream(fileOut);
				         out.writeObject(listOptions);
				         out.close();
				         fileOut.close();
				         hello = "test";
				      } catch (IOException i) {
				         i.printStackTrace();
				      }
			}
			
			
			
		}
	
	
		

	
}
