package listAnalysis;

import java.beans.EventHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventType;
import javafx.scene.control.ProgressBar;

public class DictionaryAnalysisTask extends Task<DictionaryAnalysisResult> {

	private String filePath;

	
	public DictionaryAnalysisTask(String filePath) {
		this.filePath = filePath;
		
	}
	
	@Override
	protected DictionaryAnalysisResult call() throws Exception {
		DictionaryAnalysisResult result = new DictionaryAnalysisResult();
		

		FileReader fileReader = new FileReader(filePath);
		BufferedReader reader = new BufferedReader(fileReader);
		
		String nextLine = reader.readLine().substring(41);
	    int totalLines = GetTotalLines();
	    result.setTotalWords(totalLines);
	    result.setFileName(new File(filePath).getName());
	    int currentLine = 1;
	    
	    
		while(nextLine != null) {
			
			
			result.UpdateResults(nextLine);
			
		
			String line = reader.readLine();
			
			if(line == null) {
				break;
			}else {
				nextLine = line.substring(41);
			}			

			currentLine++;
			
		   updateProgress(currentLine, totalLines);
		   
			
		}
		
		return result;
	
}
	
	
	public int GetTotalLines() throws IOException {
		
		//I needed a quick way to count the total lines in a file
		//https://stackoverflow.com/questions/1277880/how-can-i-get-the-count-of-line-in-a-file-in-an-efficient-way

		FileInputStream stream;
		try {
			stream = new FileInputStream(filePath);
			byte[] buffer = new byte[8192];
			int count = 0;
			int n;
			while ((n = stream.read(buffer)) > 0) {
			    for (int i = 0; i < n; i++) {
			        if (buffer[i] == '\n') {
			        	count++;
			        }
			    }
			}
			stream.close();

			return count;


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;

	}

	
	
	
	

}
