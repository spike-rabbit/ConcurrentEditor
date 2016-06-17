package ce.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import ce.shared.Change;
import ce.shared.ChangeSubmit;

/**
 * Handels file opend at serverstart
 *
 * @author Florian.Loddenkemper
 *
 */
public class FileHandler {

	private final static FileHandler instance = new FileHandler();

	/**
	 * gives the valid filehandler
	 *
	 * @return the filehandler
	 */
	public static FileHandler getInstance() {
		return instance;
	}

	private final Queue<Change> changes = new LinkedBlockingQueue<>();
	private final Thread changeRunner = new Thread(this::applyChanges);

	private String current = "";
	private long currentV = 0;

	/**
	 * private constructor because only one handler is allowed per server
	 */
	private FileHandler() {
		this.current = "";
		this.changeRunner.start();
	}

	/**
	 * lists all not completed changes
	 *
	 * @return Queue of type Change
	 */
	public Queue<Change> getChanges() {
		return this.changes;
	}

	/**
	 * closes thread merging input into file
	 */
	public void close() {
		this.changeRunner.interrupt();
	}

	/**
	 * merge file for changes from client
	 */
	private void applyChanges() {
		while (true) {
			Change change = this.changes.poll();
			if (change != null) {
				// Not required yet!
				// Map.Entry<ChangeKey, String> lastString =
				// this.versionControl.lastEntry();
				if (this.current.hashCode() == change.getSourceHash()) {
					StringBuffer buffer = new StringBuffer();
					switch (change.getType()) {
					case INSERT:
						if (change.getStartIndex() == 0) {
							this.current = change.getText() + this.current;
						} else {
							buffer.append(this.current.substring(0, change.getStartIndex()));
							buffer.append(change.getText());
							if (this.current.length() > change.getStartIndex()) {
								buffer.append(this.current.substring(change.getStartIndex()));
							}
							this.current = buffer.toString();
						}
						break;
					case DELETE:
						if (change.getStartIndex() != 0) {
							buffer.append(this.current.substring(0, change.getStartIndex()));
						}
						buffer.append(this.current.substring(change.getStartIndex() + change.getText().length()));
						this.current = buffer.toString();
						break;
					default:
						break;
					}
				}
				ServerGate.getInstance().sendAll(new ChangeSubmit(this.current, change.getType(),
						change.getText().length(), change.getStartIndex(), ++this.currentV));
			}

		}
	}

	public void saveFile(String path) {
		File file = new File(path);
		BufferedWriter write = null;
		try {
			if (file.exists()) {
				file.delete();
			}
			write = new BufferedWriter(new FileWriter(file));

			write.write(this.current);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getCurrent() {
		return this.current;
	}

	public long getCurrentV() {
		return this.currentV;
	}

}
