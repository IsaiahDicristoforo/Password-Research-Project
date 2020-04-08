package passwordListGeneration;

import java.util.Random;

public class LettersNumbersAndSpecialCharactersPrepender implements PrependBehavior {

	@Override
	public String prependCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		String charactersToAppend = "";
		Random r = new Random();
		for(int i = 0; i < totalCharactersToAppend; i++) {
			
			//32-126 is the ASCII printable character range
			charactersToAppend += (char)(r.nextInt((126 - 32) + 1) + 32);
		}
		
		return charactersToAppend + stringToAppendTo;
	}
	public String toString() {
		return "Letters, numbers, and special characters";
		
	}

	
}
