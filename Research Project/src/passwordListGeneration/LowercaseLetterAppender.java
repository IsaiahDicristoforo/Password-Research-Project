package passwordListGeneration;

import java.util.Random;

public class LowercaseLetterAppender implements AppendBehavior {

	@Override
	public String appendCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		
		String abc = "abcdefghijklmnopqrstuvwxyz";
		Random rd = new Random();
		
		String lettersToAppend = "";
		for(int i = 1; i <= totalCharactersToAppend; i++) {
			 
			lettersToAppend += abc.charAt(rd.nextInt(abc.length()));
		}
		return stringToAppendTo + lettersToAppend;
	}
	
	public String toString() {
		return "Lowercase letters";
		
	}

}
