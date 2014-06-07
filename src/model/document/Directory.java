package model.document;

import java.util.Iterator;
import java.util.Vector;

public class Directory extends Document {

	private Vector<Document> directory;

	public Directory(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		this.directory = new Vector<Document>();
	}

	public void addDocument(Document d) {
		this.directory.add(d);
	}

	@Override
	public int getSize() {
		int size = 0;
		Iterator<Document> itr = this.directory.iterator();
		while (itr.hasNext()) {
			Document d = (Document) itr.next();
			size += d.getSize();
		}
		return size;
	}

}
