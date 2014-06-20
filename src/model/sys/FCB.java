package model.sys;

import java.io.Serializable;
import java.util.Date;

import model.sys.Config.FILE_TYPE;

/**
 * 
 * @author Tom Hu
 * 
 */
public class FCB implements Serializable {

	private static final long serialVersionUID = -5262771405010721496L;

	public String filename; // 文件名
	public FILE_TYPE type; // FILE 或 DORECTORY
	public String address; // 文件地址
	public Date createdDate;
	public Date updatedDate;
	public int fatherBlockId; // 指向上一级FCB
	public int size; // 当前文件控制块所对应的数据区块个数，即占用空间
	public int dataStartBlockId; // 数据区Block的id
	public int blockId; // 当前FCB所在Block的id
//	public int realSize = 0; // FCB对应文件的实际大小，其中文件夹大小等于目录文件大小加上其目录下所有文件的大小

	// Constructor
	public FCB(String filename, int fatherBlockId, FILE_TYPE type, int size,
			int dataStartBlockId, int blockId) {
		this.filename = filename;
		this.fatherBlockId = fatherBlockId;
		this.type = type;
		this.size = size;
		this.createdDate = new Date();
		this.updatedDate = (Date) this.createdDate.clone();
		this.dataStartBlockId = dataStartBlockId;
		this.blockId = blockId;
	}
}
