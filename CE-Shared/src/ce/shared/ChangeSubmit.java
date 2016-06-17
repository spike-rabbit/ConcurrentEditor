package ce.shared;

import java.io.Serializable;

/**
 * submit message from server
 * @author Florian.Loddenkemper
 *
 */
public class ChangeSubmit implements Serializable {

	private static final long serialVersionUID = 5022068318798730764L;
	private final String text;
	private final long serverID;

	/**
	 * creates new changesubmit object
	 * @param text text changed
	 * @param serverID server's id
	 */
	public ChangeSubmit(String text, long serverID) {
		super();
		this.text = text;
		this.serverID = serverID;
	}

	/**
	 * String
	 * @return submitted text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * long
	 * @return server's id who submitted
	 */
	public long getServerID() {
		return this.serverID;
	}

}
