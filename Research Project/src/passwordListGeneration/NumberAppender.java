package passwordListGeneration;

import java.util.Random;

public class NumberAppender implements AppendBehavior {

	@Override
	public String appendCharacters(String stringToAppendTo, int totalCharactersToAppend) {
				
		String numsToAppend = "";
		Random r = new Random();
		
		for(int i = 1; i <= totalCharactersToAppend; i++) {
			numsToAppend += r.nextInt(10);
		}
		
		return stringToAppendTo+ numsToAppend;
	}
	
	public String toString() {
		return "Numbers only";
		
	}

}
