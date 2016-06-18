package ce.shared;

import java.io.Serializable;

/**
 * stores infos on textchanes in client used as container in network transfer
 * 
 * @author Florian.Loddenkemper
 * @author Maximilian.Koeller
 *
 */
public class Change implements Serializable {
	private static final long serialVersionUID = 944537172052687223L;
	private final String text;
	private final int startIndex;
	private final Type type;
	private final int sourceHash;
	private final long serverID;

	/**
	 * creates new Change Object
	 * 
	 * @param text
	 *            stores the text which was changed
	 * @param startIndex
	 *            index showing position where you can find text in the changed
	 *            file
	 * @param type
	 *            refers to change type(Insert or Delete)
	 * @param sourceHash
	 *            hash value of loaded text before change
	 * @param serverID
	 *            stores id of an server
	 */
	public Change(String text, int startIndex, Type type, int sourceHash, long serverID) {
		super();
		this.text = text;
		this.startIndex = startIndex;
		this.type = type;
		this.sourceHash = sourceHash;
		this.serverID = serverID;
	}

	/**
	 * String text
	 * 
	 * @return text which was changed
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * int
	 * 
	 * @return index where you find change in source
	 */
	public int getStartIndex() {
		return this.startIndex;
	}

	/**
	 * insert/delete
	 * 
	 * @return type of change
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * int
	 * 
	 * @return source string hash value
	 */
	public int getSourceHash() {
		return this.sourceHash;
	}

	/**
	 * long
	 * 
	 * @return id of connected server
	 */
	public long getSeverID() {
		return this.serverID;
	}

	@Override
	public String toString() {
		return "Change [text=" + this.text + ", startIndex=" + this.startIndex + ", type=" + this.type + ", serverID="
				+ this.serverID + "]";
	}

}
