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

	private String text;

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

			}
		}
	}

}
