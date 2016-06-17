package ce.shared;

import java.io.Serializable;

/**
 * submit message from server
 *
 * @author Florian.Loddenkemper
 *
 */
public class ChangeSubmit implements Serializable {

	private static final long serialVersionUID = 5022068318798730764L;
	private final String text;
	private final Type type;
	private final int length;
	private final int index;
	private final long serverID;

	public ChangeSubmit(String text, Type type, int length, int index, long serverID) {
		super();
		this.text = text;
		this.type = type;
		this.length = length;
		this.index = index;
		this.serverID = serverID;
	}

	/**
	 * String
	 *
	 * @return submitted text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * long
	 *
	 * @return server's id who submitted
	 */
	public long getServerID() {
		return this.serverID;
	}

	public Type getType() {
		return this.type;
	}

	public int getLength() {
		return this.length;
	}

	public int getIndex() {
		return this.index;
	}

}
