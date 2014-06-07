package model.document;

public class File extends Document {

	private StringBuffer data;

	public File(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.data = new StringBuffer();
	}
	
	public void save(String s) {
		if (this.data.length() != 0) {
			this.data = this.data.delete(0, this.data.length() - 1);
		}
		this.data.append(s);
	}

	@Override
	public int getSize() {
		this.size = this.data.length();
		return super.getSize();
	}

}
