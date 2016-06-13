package ce.server;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import ce.shared.Change;

public class FileHandler {

	private final static FileHandler instance = new FileHandler();

	public static FileHandler getInstance() {
		return instance;
	}

	private final Queue<Change> changes = new LinkedBlockingQueue<>();
	private final Thread changeRunner = new Thread(this::applyChanges);

	private String current;

	private FileHandler() {
		this.changeRunner.start();
	}

	public Queue<Change> getChanges() {
		return this.changes;
	}

	private void applyChanges() {
		while (true) {
			Change change = this.changes.poll();
			if (change != null) {
				switch(change.getType()){
				case INSERT:
					current = current.substring(0,change.getStartIndex()) + change.getText() + current.substring(change.getStartIndex(), current.length() -1);
					break;
				case DELETE:
					if(change.getText() == current.substring(change.getStartIndex(), change.getText().length()-1)){
						current = current.substring(0,change.getStartIndex()) + current.substring(change.getStartIndex(), current.length()-1);	
					}
					else{
						int leftMatch = 0;
						int rightMatch = 0;
						for(int i = 0;change.getStartIndex() - i >= 0 && i + change.getStartIndex() < current.length(); i++){
							if(change.getStartIndex() - i >= 0 && change.getText().charAt(leftMatch)== current.charAt(change.getStartIndex() + 1)){
								rightMatch++;
							}
							
							if(leftMatch == change.getText().length()){
								current = current.substring(0,change.getStartIndex() - leftMatch) + current.substring(change.getStartIndex() - leftMatch + change.getText().length(), current.length());
							}
							else if(rightMatch == change.getText().length()){
								current = current.substring(0, change.getStartIndex() + rightMatch) + current.substring(change.getStartIndex() + rightMatch + change.getText().length());
							}
						}
					}
					break;
				}
			}
		}
	}

}
