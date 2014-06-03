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

}
