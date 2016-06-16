package ce.shared;

public class Change {
	private final String text;
	private final int startIndex;
	private final Type type;
	private final int sourceHash;
	private final long serverID;

	public Change(String text, int startIndex, Type type, int sourceHash, long serverID) {
		super();
		this.text = text;
		this.startIndex = startIndex;
		this.type = type;
		this.sourceHash = sourceHash;
		this.serverID = serverID;
	}

	public String getText() {
		return this.text;
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public Type getType() {
		return this.type;
	}

	public int getSourceHash() {
		return this.sourceHash;
	}
	
	public long getSeverID(){
		return this.serverID;
	}

}
