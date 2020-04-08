package passwordListGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class ListItemGenerator {

	
	private AppendBehavior appendBehavior;
	private PrependBehavior prependBehavior;
	private WordSource wordSource;
	private int totalPasswordsToGenerate;
	private ArrayList<String> hashedPasswords;
	private final String PEOPLES_NAMES_FILE_PATH = "PasswordGeneration/CommonNames.txt";
	private final String DICTIONARY_FILE_PATH = "PasswordGeneration/EnglishDictionary.txt";
	private int totalCharactersToAppend;
	private int totalChatactersToPrepend;
	
	
	public ListItemGenerator(int totalPasswordsToGenerate, int totalCharactersToAppend, int totalCharactersToPrepend, AppendBehavior appendBehavior, PrependBehavior prependBehavior, WordSource wordSource) {
		
		hashedPasswords = new ArrayList<String>();
		this.totalPasswordsToGenerate = totalPasswordsToGenerate;
		this.setAppendBehavior(appendBehavior);
		this.setPrependBehavior(prependBehavior);
		this.setSource(wordSource);
		this.setTotalCharactersToAppend(totalCharactersToAppend);
		this.setTotalCharactersToPrepend(totalCharactersToPrepend);
	}
	
	public ArrayList<String> GeneratePasswords(){
	
		ArrayList<String> passwordsToReturn = new ArrayList<String>();
		
		BufferedReader reader = null;
		String pathName = "";
		if(this.wordSource != WordSource.RandomCharacters) {
			if(this.wordSource == WordSource.DictionaryWord) {
				pathName = DICTIONARY_FILE_PATH;
			}
			else if(this.wordSource == WordSource.PersonName) {
				pathName = PEOPLES_NAMES_FILE_PATH;
			}
				java.nio.file.Path path = Paths.get(pathName);
				
				try {
				    int totalWords = Files.readAllLines(path, StandardCharsets.ISO_8859_1).size();
					for(int i = 1; i <= totalPasswordsToGenerate; i++) {
						reader = new BufferedReader(new FileReader(pathName));
						int randomIndex = new Random().nextInt(totalWords);
					
						int index = 0;
						String randomString = "";
						
						while(true) {
							String line = reader.readLine();
							if(index == randomIndex) {
								randomString = line;
								break;
							}
							index++;
							
						}
						
						String output = appendBehavior.appendCharacters(randomString, this.totalCharactersToAppend);
						output = prependBehavior.prependCharacters(output, totalChatactersToPrepend);
						passwordsToReturn.add(new SHA1HashGenerator().generateHash(output) + " " + output);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		}else {
			Random r = new Random();
			int randomLength = r.nextInt(10);
			
			String result = "";
			
			for(int i = 0; i <= totalPasswordsToGenerate; i++) {
				randomLength = r.nextInt(10) + 4;
				for(int j = 0; j <= randomLength; j++) {
					//32-126 is the ASCII printable character range
					result += (char)(r.nextInt((126 - 32) + 1) + 32);
				}
				passwordsToReturn.add(new SHA1HashGenerator().generateHash(result) + " " + result);
				result = "";
			}
		}
	
		return passwordsToReturn;
		
	}
	
	public String GenerateRandomWordFromFile(String filePath){
		String randomString = "";
		try {
			
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return randomString;
		
	}
	
	


	public WordSource getSource() {
		return this.wordSource;
	}


	public void setSource(WordSource source) {
		this.wordSource = source;
	}


	public int getTotalPasswordsToGenerate() {
		return totalPasswordsToGenerate;
	}


	public void setTotalPasswordsToGenerate(int totalPasswordsToGenerate) {
		this.totalPasswordsToGenerate = totalPasswordsToGenerate;
	}


	public PrependBehavior getPrependBehavior() {
		return prependBehavior;
	}


	public void setPrependBehavior(PrependBehavior prependBehavior) {
		this.prependBehavior = prependBehavior;
	}


	public AppendBehavior getAppendBehavior() {
		return appendBehavior;
	}


	public void setAppendBehavior(AppendBehavior appendBehavior) {
		this.appendBehavior = appendBehavior;
	}


	public void setTotalCharactersToAppend(int totalCharactersToAppend) {
		this.totalCharactersToAppend = totalCharactersToAppend;
	}
	public int getTotalCharactersToAppend() {
		return this.totalCharactersToAppend;
	}
	public int getTotalCharactersToPrepend() {
		return this.totalChatactersToPrepend;
	}
	public void setTotalCharactersToPrepend(int totalCharactersToPrepend) {
		this.totalChatactersToPrepend = totalCharactersToPrepend;
	}
	
	
	
	

}
