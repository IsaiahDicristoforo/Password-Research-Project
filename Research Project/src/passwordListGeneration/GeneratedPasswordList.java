package passwordListGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeneratedPasswordList extends ArrayList<String> {
	
	private String filePath;
	
	
	public GeneratedPasswordList(String filePath, ArrayList<String> itemsToAdd) {
		this.addAll(itemsToAdd);
		this.filePath = filePath;
	}
	public GeneratedPasswordList(String filePath) {
		this.filePath = filePath;
	}
	
	public void  WriteListToFile() {
		
		
			File newFile = new File(filePath + "/PasswordList.txt");
			
			if(!newFile.exists()) {
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				newFile.delete();
				try {
					newFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(newFile.getPath()));
				for(String s : this) {
					writer.write(s);
					writer.newLine();
				}
				writer.close();
			} catch (IOException e) {
				System.out.println("ERROR Writing To File..." + e.getLocalizedMessage());
			}
			
		
		
		
		

		
		
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	

}
