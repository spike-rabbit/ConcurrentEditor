package ce.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import ce.shared.Change;

public class FileHandler {

	private final static FileHandler instance = new FileHandler();

	public static FileHandler getInstance() {
		return instance;
	}
	private final NavigableMap<K, String>
	private final Queue<Change> changes = new LinkedBlockingQueue<>();
	private final Thread changeRunner = new Thread(this::applyChanges);

	private String current;
	
	
	private FileHandler() {
		this.changeRunner.start();
		
	}

	public Queue<Change> getChanges() {
		return this.changes;
	}

	public void close() {
		this.changeRunner.interrupt();
	}

	private void applyChanges() {
		String toUpdate;
		while (true) {
			
			Change change = this.changes.poll();
			toUpdate = getTextToUpdate(change.getText().hashCode());
			if (change != null) {
				switch(change.getType()){
				case INSERT:
					toUpdate = toUpdate.substring(0,change.getStartIndex()) + change.getText() + toUpdate.substring(change.getStartIndex(), toUpdate.length() -1);
					break;
				case DELETE:
					if(change.getText() == toUpdate.substring(change.getStartIndex(), change.getText().length()-1)){
						toUpdate = toUpdate.substring(0,change.getStartIndex()) + toUpdate.substring(change.getStartIndex(), toUpdate.length()-1);	
					}
					else{
						int leftMatch = 0;
						int rightMatch = 0;
						for(int i = 0;change.getStartIndex() - i >= 0 && i + change.getStartIndex() < toUpdate.length(); i++){
							if(change.getStartIndex() - i >= 0 && change.getText().charAt(leftMatch)== toUpdate.charAt(change.getStartIndex() + 1)){
								rightMatch++;
							}
							
							if(leftMatch == change.getText().length()){
								toUpdate = toUpdate.substring(0,change.getStartIndex() - leftMatch) + toUpdate.substring(change.getStartIndex() - leftMatch + change.getText().length(), current.length());
							}
							else if(rightMatch == change.getText().length()){
								toUpdate = toUpdate.substring(0, change.getStartIndex() + rightMatch) + toUpdate.substring(change.getStartIndex() + rightMatch + change.getText().length());
							}
						}
					}
					break;
				}
			}
		}
	}
	
	private String getTextToUpdate(int hashCode){
		return "";
	}
		
	public void saveFile(String path){
		File file = new File(path);
		BufferedWriter write = null;
		try {
			if(file.exists()){
				file.delete();
			}
			write = new BufferedWriter(new FileWriter(file));
			
			write.write(current);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	private static class ChangeKey{
		
	}

}
