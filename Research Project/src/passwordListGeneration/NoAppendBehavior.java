package passwordListGeneration;

public class NoAppendBehavior implements AppendBehavior {

	@Override
	public String appendCharacters(String stringToAppendTo, int totalCharactersToAppend) {
		return stringToAppendTo;
	}
	
	public String toString() {
		return "Append no characters";
		
	}
	

}
