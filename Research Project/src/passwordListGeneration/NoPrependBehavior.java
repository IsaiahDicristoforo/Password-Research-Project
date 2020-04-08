package passwordListGeneration;

public class NoPrependBehavior implements PrependBehavior {

	@Override
	public String prependCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		return stringToAppendTo;
	}
	
	public String toString() {
		return "Prepend no characters";
		
	}

}
