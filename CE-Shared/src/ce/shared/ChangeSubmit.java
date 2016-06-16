package ce.shared;

import java.io.Serializable;

public class ChangeSubmit implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5022068318798730764L;
	private final String text;
	private final long serverID;

	public ChangeSubmit(String text, long serverID) {
		super();
		this.text = text;
		this.serverID = serverID;
	}

	public String getText() {
		return this.text;
	}

	public long getServerID() {
		return this.serverID;
	}

}
