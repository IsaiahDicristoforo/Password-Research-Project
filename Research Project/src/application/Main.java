package application;
	
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.nulabinc.zxcvbn.Zxcvbn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import passwordListGeneration.SHA1HashGenerator;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		
		
		try {
			
		/*	BufferedReader reader = new BufferedReader(new FileReader("C:\\Dictionary Research Project\\Rocktastic12a\\Rocktastic12a.txt"));
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Dictionary Research Project\\hashedRocktastic.txt"));
			
			String s = reader.readLine();
			while(s != null) {
				writer.write(new SHA1HashGenerator().generateHash(s) + " " + s);
				writer.newLine();
				s= reader.readLine();
			}
			*/
								 
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Application.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
	}
}
