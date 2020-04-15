package listAnalysis;

import java.util.HashMap;

import DictionaryListPaths.DictionaryList;

public class DictionaryAnalysisResult {

	private String fileName;
	private double fileSize;
	
	
	private int totalWords;
	
	private int totalWordsThatStartWithLetter;
	private int totalWordsStartingWithNonLetter;
	
	private int totalWordsWithAtSign;
	private int totalWordsWithPoundSign;
	private int totalWordsWithExclamationPoint;
	private int totalWordsWithNoSpecialCharacter;
	

	// Key, value
	private HashMap<Integer, Integer> wordLengths;

	public DictionaryAnalysisResult() {
		wordLengths = new HashMap<Integer, Integer>();
	}
	
	public void UpdateResults(String wordToAdd) {
		AddWordToLengthTracker(wordToAdd);
		if(Character.isLetter(wordToAdd.charAt(0))) {
			totalWordsThatStartWithLetter++;
		} else{
			totalWordsStartingWithNonLetter++;
		}
		
		if(wordToAdd.contains("@")) {
			totalWordsWithAtSign++;
		}
		if(wordToAdd.contains("#")) {
			totalWordsWithPoundSign++;
		}
		if(wordToAdd.contains("!")) {
			totalWordsWithExclamationPoint++;
		}
	}
	
	public int getTotalWordsBeginningWithLetter() {
		return totalWordsThatStartWithLetter;
	}
	public int getTotalWordsStartingWithNonLetter() {
		return totalWordsStartingWithNonLetter;
	}
	
	

	private void AddWordToLengthTracker(String wordToAdd) {

		int length = wordToAdd.length();

		if (wordLengths.containsKey(length)) {

			wordLengths.replace(length, wordLengths.get(length) + 1);
		} else {
			wordLengths.put(wordToAdd.length(), 1);
		}

	}

	public HashMap<Integer, Integer> getWordLengths() {
		return wordLengths;
	}

	public void setWordLengths(HashMap<Integer, Integer> wordLengths) {
		this.wordLengths = wordLengths;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getFileSize() {
		return fileSize;
	}

	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getTotalWords() {
		return totalWords;
	}
	
	
	public void setTotalWords(int totalWords) {
		this.totalWords = totalWords;
	}

	public int getTotalWordsWithAtSign() {
		return totalWordsWithAtSign;
	}

	public void setTotalWordsWithAtSign(int totalWordsWithAtSign) {
		this.totalWordsWithAtSign = totalWordsWithAtSign;
	}

	public int getTotalWordsWithPoundSign() {
		return totalWordsWithPoundSign;
	}

	public void setTotalWordsWithPoundSign(int totalWordsWithPoundSign) {
		this.totalWordsWithPoundSign = totalWordsWithPoundSign;
	}

	public int getTotalWordsWithExclamationPoint() {
		return totalWordsWithExclamationPoint;
	}

	public void setTotalWordsWithExclamationPoint(int totalWordsWithExclamationPoint) {
		this.totalWordsWithExclamationPoint = totalWordsWithExclamationPoint;
	}

	public int getTotalWordsWithNoSpecialCharacter() {
		return totalWordsWithNoSpecialCharacter;
	}

	public void setTotalWordsWithNoSpecialCharacter(int totalWordsWithNoSpecialCharacter) {
		this.totalWordsWithNoSpecialCharacter = totalWordsWithNoSpecialCharacter;
	}
	

}
