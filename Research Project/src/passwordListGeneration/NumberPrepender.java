package passwordListGeneration;

import java.util.Random;

public class NumberPrepender implements PrependBehavior {
	
	@Override
	public String prependCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		
		String nums = "123456789";
		
		String numsToAppend = "";
		Random r = new Random();
		
		for(int i = 1; i <= totalCharactersToAppend; i++) {
			numsToAppend += r.nextInt(10);
		}
		
		return numsToAppend + stringToAppendTo;
	}
	
	public String toString() {
		return "Numbers only";
		
	}

}
