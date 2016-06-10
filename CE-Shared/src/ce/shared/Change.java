package ce.shared;

public class Change {
	private final String text;
	private final int startIndex;
	private final Type type;

	public Change(String text, int startIndex, Type type) {
		super();
		this.text = text;
		this.startIndex = startIndex;
		this.type = type;
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

}
