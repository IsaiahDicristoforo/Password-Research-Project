package DictionaryAttack;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import passwordListGeneration.SHA1HashGenerator;

public class SearchPartOfDictionaryTask extends Task<String> {

	private String filePath;
	private int startIndex;
	private int endIndex;
	private CountDownLatch lock;
	private String hashedPassword;
	
	public SearchPartOfDictionaryTask(String filePath, String hashedPassword,int startIndex, int endIndex, CountDownLatch lock) {
	
		this.lock = lock;
		this.hashedPassword = hashedPassword;
		this.filePath = filePath;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				lock.countDown();
				
			}
		});
	}
	
	@Override
	protected String call() throws Exception {
		
		Stream<String> s;
		try (Stream<String> all_lines = Files.lines(Paths.get(filePath),StandardCharsets.ISO_8859_1)) {
			
			  s= all_lines.skip(startIndex);
			  Iterator<String> streamIterator = s.iterator();
			 // int lineNum = startIndex;
			  while(startIndex < endIndex && !this.isCancelled()){
				 String line =  streamIterator.next();	
				 //lineNum++;
				 startIndex++;
				
				// String dictionaryHash = new SHA1HashGenerator().generateHash(line);
				 if(line.substring(0,40).equals(hashedPassword.substring(0,40))) {

					 while(lock.getCount() > 0) {
						 lock.countDown();
					 }
					 //System.out.println(line + " FOUND");
					 return line.substring(41);
					 
					 
				 }
				  
			  }
			  
			
		}catch(Exception e) {
		}
		//System.out.println("failed");
		  return null;

	}

}
