package controller;

import java.nio.ByteBuffer;
import java.util.Vector;

import com.google.gson.Gson;

import model.sys.Config;
import model.sys.FCB;

public class SystemCore {

	private DiskManager diskManager;
	private FCB currentDirFCB;
	private FCB[] currentDir;
	private int fileCount = 0;

	/**
	 * 构造函数
	 */
	public SystemCore() {
		super();
		// TODO Auto-generated constructor stub
		this.diskManager = new DiskManager();

		// 初始化
		this.diskManager.init();

		Gson gson = new Gson();

		// 读取根目录FCB
		ByteBuffer currentDirFCBBuffer = this.diskManager.io.read(2, 1);
		String currentDirFCBJSON = new String(currentDirFCBBuffer.array(),
				Config.CHARSET);
		this.currentDirFCB = gson.fromJson(currentDirFCBJSON, FCB.class);

		// 读取根目录目录文件
		ByteBuffer currentDirJSONBuffer = this.diskManager.io.read(
				this.currentDirFCB.dataStartBlockId, this.currentDirFCB.size);
		String currentDirJSON = new String(currentDirJSONBuffer.array(),
				Config.CHARSET);
		this.currentDir = gson.fromJson(currentDirJSON, FCB[].class);
		
		for (int i = 0; i < this.currentDir.length; i++) {
			if (this.currentDir[i] != null) {
				fileCount++;
			} else {
				break;
			}
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public void createFile(String filename) {
		int fcbBlockStart = this.diskManager.getFreeSpace(1);
		int fileDataBlockStart = this.diskManager.getFreeSpace(
				Config.FILE_MAX_BLOCKS, fcbBlockStart + 1);

		if (fcbBlockStart != 0 && fileDataBlockStart != 0) {
			FCB fcb = new FCB(filename, this.currentDirFCB.blockId,
					Config.FILE_TYPE.FILE, Config.FILE_MAX_BLOCKS,
					fileDataBlockStart, fcbBlockStart);
			
			// 申请分配空间
			this.diskManager.alloc(fcbBlockStart, 1);
			this.diskManager.alloc(fileDataBlockStart, Config.FILE_MAX_BLOCKS);
			
			// 当前文件夹下加入该文件
			this.currentDir[fileCount++] = fcb;
		}
	}

}
