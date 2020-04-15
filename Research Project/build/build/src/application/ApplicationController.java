package application;

import java.beans.EventHandler;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ApplicationController {
	@FXML
	private Button btn_CreateNewDictionaryTest;
	
	@FXML 
	public void OnClick(ActionEvent e) {
		
		Parent root;
		try {


			FXMLLoader loader = new FXMLLoader(getClass().getResource("DictionarySelector.fxml"));
			 root = loader.load();
			DictionarySelectorController controller = loader.<DictionarySelectorController>getController();

			
			Scene scene = new Scene(root);

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.showAndWait();
					
		
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		
		
}

}
