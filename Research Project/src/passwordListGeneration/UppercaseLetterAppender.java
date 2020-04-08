package passwordListGeneration;

import java.util.Random;

public class UppercaseLetterAppender implements AppendBehavior {
	
	@Override
	public String appendCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rd = new Random();
		
		String lettersToAppend = "";
		for(int i = 1; i <= totalCharactersToAppend; i++) {
			 
			lettersToAppend += abc.charAt(rd.nextInt(abc.length()));
		}
		return stringToAppendTo + lettersToAppend;
	}
	
	public String toString() {
		return "Uppercase Letters only";
		
	}

}
