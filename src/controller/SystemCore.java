package controller;

import java.nio.ByteBuffer;

import model.sys.Config;
import model.sys.Config.FILE_TYPE;
import model.sys.FCB;

import com.google.gson.Gson;

public class SystemCore {

	private DiskManager diskManager;
	public FCB currentDirFCB;
	public FCB[] currentDir;
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
		if (!this.checkFilename(filename, FILE_TYPE.FILE)) {
			return;
		}

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

			Gson gson = new Gson();

			// 更新当前目录文件
			this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));

			// 将新建文件的FCB写入Block
			this.diskManager.io.write(fcbBlockStart, 1, gson.toJson(fcb));

			// 为新建的文件写内容
			this.updateFile(fcb, "");
		} else {
			System.out.println("空间不够");
		}
	}

	/**
	 * 向FCB所指向的数据区更新内容
	 * 
	 * @param fcb
	 *            需要更新的文件的FCB
	 * @param content
	 *            所更新的内容
	 */
	public void updateFile(FCB fcb, String content) {
		this.diskManager.io.write(fcb.dataStartBlockId, fcb.size, content);
	}

	/**
	 * 创建文件夹
	 * 
	 * @param filename
	 *            文件夹名
	 */
	public void createDir(String filename) {
		if (!this.checkFilename(filename, FILE_TYPE.DIRECTORY)) {
			return;
		}

		int fcbBlockStart = this.diskManager.getFreeSpace(1);
		int fileDataBlockStart = this.diskManager.getFreeSpace(
				Config.FILE_MAX_BLOCKS, fcbBlockStart + 1);

		if (fcbBlockStart != 0 && fileDataBlockStart != 0) {
			FCB dirFcb = new FCB(filename, this.currentDirFCB.blockId,
					Config.FILE_TYPE.FILE, Config.FILE_MAX_BLOCKS,
					fileDataBlockStart, fcbBlockStart);
			FCB[] dir = new FCB[40];

			// 申请分配空间
			this.diskManager.alloc(fcbBlockStart, 1);
			this.diskManager.alloc(fileDataBlockStart, Config.FILE_MAX_BLOCKS);

			// 当前文件夹下加入该文件
			this.currentDir[fileCount++] = dirFcb;

			Gson gson = new Gson();

			// 更新当前目录文件
			this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));

			// 将新建文件的FCB写入Block
			this.diskManager.io.write(fcbBlockStart, 1, gson.toJson(dirFcb));

			// 为新建的文件写内容
			this.updateFile(dirFcb, gson.toJson(dir));
		} else {
			System.out.println("空间不够");
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filename
	 *            文件夹名
	 */
	public void deleteDir(String filename) {
		FCB fcb = this.getFCB(filename, FILE_TYPE.DIRECTORY);

		if (fcb == null) {
			return;
		}

		// 删除对应的FCB
		this.deleteFCB(fcb);

		// 递归删除
		this.recursiveDeleteDir(fcb);
	}

	/**
	 * 递归删除文件夹以及文件夹内所有文件
	 * 
	 * @param fcb
	 *            需要删除的文件夹对应的FCB
	 */
	private void recursiveDeleteDir(FCB fcb) {
		// 释放空间
		// 释放该FCB所在的块
		this.diskManager.free(fcb.blockId, 1);
		// 释放该FCB所指向的数据区
		this.diskManager.free(fcb.dataStartBlockId, fcb.size);

		Gson gson = new Gson();

		// 获取目录下的所有文件
		ByteBuffer bf = this.diskManager.io
				.read(fcb.dataStartBlockId, fcb.size);
		FCB[] dir = gson.fromJson(new String(bf.array(), Config.CHARSET),
				FCB[].class);

		// 遍历，删除
		for (int i = 0; i < dir.length; i++) {
			if (dir[i].type == FILE_TYPE.DIRECTORY) {
				// 递归调用
				this.recursiveDeleteDir(dir[i]);
			} else {
				// 删除文件
				// 释放空间
				// 释放该FCB所在的块
				this.diskManager.free(fcb.blockId, 1);
				// 释放该FCB所指向的数据区
				this.diskManager.free(fcb.dataStartBlockId, fcb.size);
			}
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filename
	 *            文件名
	 */
	public void deleteFile(String filename) {
		FCB fcb = this.getFCB(filename, FILE_TYPE.FILE);

		if (fcb == null) {
			return;
		}

		// 删除对应的FCB
		this.deleteFCB(fcb);

		// 释放空间
		// 释放该FCB所在的块
		this.diskManager.free(fcb.blockId, 1);
		// 释放该FCB所指向的数据区
		this.diskManager.free(fcb.dataStartBlockId, fcb.size);
	}

	/**
	 * 删除当前文件夹下FCB，并更新Block数据
	 * 
	 * @param fcb
	 */
	private void deleteFCB(FCB fcb) {
		int i = 0;
		for ( ; i < this.currentDir.length; i++) {
			if (this.currentDir[i].filename.equals(fcb.filename)
					&& this.currentDir[i].type == fcb.type) {
				this.currentDir[i] = null;
				break;
			}
		}
		
		// 保持文件目录的连续
		while (i < this.currentDir.length - 1) {
			if (this.currentDir[i + 1] == null) {
				break;
			}
			this.currentDir[i] = this.currentDir[i++];
			this.currentDir[i] = null;
		}
		
		Gson gson = new Gson();
		
		// 写回Block
		this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));
	}

	/**
	 * 在当前目录下通过文件名找FCB
	 * 
	 * @param filename
	 *            文件名
	 * @return 找到的FCB
	 */
	private FCB getFCB(String filename, FILE_TYPE type) {
		for (int i = 0; i < this.currentDir.length; i++) {
			if (this.currentDir[i].filename == filename
					&& this.currentDir[i].type == type) {
				return this.currentDir[i];
			}
		}
		return null;
	}

	/**
	 * 检查文件名，不允许一样的名字
	 * 
	 * @param filename
	 *            文件名
	 * @param type
	 *            文件类型
	 * @return 布尔值
	 */
	private boolean checkFilename(String filename, FILE_TYPE type) {
		for (int i = 0; i < this.currentDir.length; i++) {
			if (this.currentDir[i].filename == filename
					&& this.currentDir[i].type == type) {
				return false;
			}
		}
		return true;
	}

}
