package DictionaryListPaths;

import java.io.Serializable;
import java.util.ArrayList;

public class ListTracker implements Serializable {

	private ArrayList<DictionaryList> listOfDictionaryLists;
	
	public ListTracker() {
		listOfDictionaryLists = new ArrayList<DictionaryList>();
	}
	
	public void addDictionaryList(DictionaryList listToAdd) {
		listOfDictionaryLists.add(listToAdd);
	}
	
	public ArrayList<DictionaryList> getItems(){
		return listOfDictionaryLists;
	}
	
	
}
