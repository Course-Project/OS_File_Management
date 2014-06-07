package model.document;

public abstract class Document {

	protected String name = "";
	protected int size = 0;

	public Document(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return this.size;
	}

}
