package passwordListGeneration;

import java.util.Random;

public class UppercaseLetterPrepender implements PrependBehavior {

	@Override
	public String prependCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		
		String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rd = new Random();
		
		String lettersToAppend = "";
		for(int i = 1; i <= totalCharactersToAppend; i++) {
			 
			lettersToAppend += abc.charAt(rd.nextInt(abc.length()));
		}
		return lettersToAppend + stringToAppendTo;
		
	}
	
	public String toString() {
		return "Uppercase letters only";
		
	}


}
