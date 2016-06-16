package ce.shared;

public class ChangeSubmit {
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
	
	public long getServerID(){
		return this.serverID;
	}

}
