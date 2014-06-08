package model.sys;

import java.util.Date;

import model.sys.Config.FILE_TYPE;

public class FCB {

	public String filename; // filename
	public FILE_TYPE type; // FILE or DORECTORY
	public String address; // file address - absolute address
	public Date createdDate;
	public Date updatedDate;
	public int length;
	
	// Constructor
	public FCB(String filename, FILE_TYPE type, int size) {
		this.filename = filename;
		this.type = type;
		this.length = size;
		this.createdDate = new Date();
		this.updatedDate = (Date) this.createdDate.clone();
	}

}
