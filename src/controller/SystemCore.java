package controller;

import java.nio.ByteBuffer;
import java.util.Date;

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
		this.diskManager = new DiskManager();

		// 初始化系统核心
		this.init();
	}

	/**
	 * 初始化系统核心
	 */
	private void init() {
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

		// 计算文件个数
		this.countFiles();
	}

	/**
	 * 创建文件
	 * 
	 * @param filename
	 *            文件名
	 * @return 是否创建成功
	 */
	public boolean createFile(String filename) {
		if (!this.checkFilename(filename, FILE_TYPE.FILE)) {
			return false;
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

			// 更新目录FCB
			this.updateFCB(this.currentDirFCB);

			// 更新当前目录文件
			this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));

			// 将新建文件的FCB写入Block
			this.diskManager.io.write(fcbBlockStart, 1, gson.toJson(fcb));

			// 为新建的文件写内容
			this.updateFile(fcb, "");
		} else {
			System.out.println("空间不够");

			return false;
		}

		return true;
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
	 * 独处文件内容
	 * 
	 * @param fcb
	 *            文件对应的FCB
	 * @return 返回文件内容
	 */
	public String readFile(FCB fcb) {
		return this.diskManager.readFile(fcb);
	}

	/**
	 * 获取格式化后的文件信息
	 * 
	 * @param fcb
	 *            指定的FCB
	 * @return 格式化后的文件信息
	 */
	public String getFileInfo(FCB fcb) {
		String result = "";

		result += ("Name: " + fcb.filename + "\n");
		result += ("Type: "
				+ (fcb.type == FILE_TYPE.FILE ? "file" : "directory") + "\n");
		result += ("Path: " + this.getCurrentPath() + "\n");
		result += ("Created: " + fcb.createdDate + "\n");
		result += ("Updated: " + fcb.updatedDate);

		return result;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param filename
	 *            文件夹名
	 * @return 是否创建成功
	 */
	public boolean createDir(String filename) {
		if (!this.checkFilename(filename, FILE_TYPE.DIRECTORY)) {
			// 有重名
			return false;
		}

		int fcbBlockStart = this.diskManager.getFreeSpace(1);
		int fileDataBlockStart = this.diskManager.getFreeSpace(
				Config.FILE_MAX_BLOCKS, fcbBlockStart + 1);

		if (fcbBlockStart != 0 && fileDataBlockStart != 0) {
			FCB dirFcb = new FCB(filename, this.currentDirFCB.blockId,
					Config.FILE_TYPE.DIRECTORY, Config.FILE_MAX_BLOCKS,
					fileDataBlockStart, fcbBlockStart);
			FCB[] dir = new FCB[40];

			// 申请分配空间
			this.diskManager.alloc(fcbBlockStart, 1);
			this.diskManager.alloc(fileDataBlockStart, Config.FILE_MAX_BLOCKS);

			// 当前文件夹下加入该文件
			this.currentDir[fileCount++] = dirFcb;

			Gson gson = new Gson();

			// 更新目录FCB
			this.updateFCB(this.currentDirFCB);

			// 更新当前目录文件
			this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));

			// 将新建文件的FCB写入Block
			this.diskManager.io.write(fcbBlockStart, 1, gson.toJson(dirFcb));

			// 为新建的文件写内容
			this.updateFile(dirFcb, gson.toJson(dir));
		} else {
			System.out.println("空间不够");

			return false;
		}

		return true;
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

		// 更新目录FCB
		this.updateFCB(this.currentDirFCB);

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
			if (dir[i] == null) {
				// 如果目录为空，直接跳出
				break;
			}

			if (dir[i].type == FILE_TYPE.DIRECTORY) {
				// 删除文件夹
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

		// 更新目录FCB
		this.updateFCB(this.currentDirFCB);

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
		for (; i < this.currentDir.length; i++) {
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
			this.currentDir[i] = this.currentDir[++i];
			this.currentDir[i] = null;
		}

		Gson gson = new Gson();

		// 写回Block
		this.updateFile(this.currentDirFCB, gson.toJson(this.currentDir));
	}

	/**
	 * 进入下一级文件夹
	 * 
	 * @param dirname
	 *            文件夹名
	 * @return 是否进入成功
	 */
	public boolean enterDir(String dirname) {
		FCB dirFCB = this.getFCB(dirname, FILE_TYPE.DIRECTORY);
		if (dirFCB == null) {
			// 所对应文件夹不存在
			System.out.println("找不到该目录");
			return false;
		}

		Gson gson = new Gson();

		// 替换currentDirFCB和currentDir
		this.currentDirFCB = dirFCB;
		ByteBuffer bf = this.diskManager.io.read(
				this.currentDirFCB.dataStartBlockId, this.currentDirFCB.size);
		String json = new String(bf.array(), Config.CHARSET);
		FCB[] dir = gson.fromJson(json, FCB[].class);
		this.currentDir = dir;

		// 计算文件个数
		this.countFiles();

		return true;
	}

	/**
	 * 返回上一级文件夹
	 * 
	 * @return 是否返回成功
	 */
	public boolean leaveDir() {
		if (this.currentDirFCB.fatherBlockId == -1) {
			// 根目录，无法返回
			System.out.println("已经到根目录，不存在上级目录");
			return false;
		}

		Gson gson = new Gson();

		// 获取上一级文件夹的FCB
		ByteBuffer bf = this.diskManager.io.read(
				this.currentDirFCB.fatherBlockId, 1);
		FCB fcb = gson.fromJson(new String(bf.array(), Config.CHARSET),
				FCB.class);

		// 获取上一级文件夹的目录文件
		// 即获取包含其目录下所有FCB的数组
		ByteBuffer dirBf = this.diskManager.io.read(fcb.dataStartBlockId,
				fcb.size);
		FCB[] dir = gson.fromJson(new String(dirBf.array(), Config.CHARSET),
				FCB[].class);

		// 替换currentDirFCB和currentDir
		this.currentDir = dir;
		this.currentDirFCB = fcb;

		// 计算文件个数
		this.countFiles();

		return true;
	}

	/**
	 * 获取当前文件夹的路径
	 * 
	 * @return
	 */
	public String getCurrentPath() {
		if (this.currentDirFCB.fatherBlockId == -1) {
			return "/";
		}
		return this.recursiveGetPath(this.currentDirFCB);
	}

	/**
	 * 通过给定FCB，递归向上获取该FCB的路径
	 * 
	 * @param fcb
	 *            给定的FCB
	 * @return 给定FCB的路径
	 */
	private String recursiveGetPath(FCB fcb) {
		if (fcb.type != FILE_TYPE.DIRECTORY) {
			return null;
		}

		if (fcb.fatherBlockId == -1) {
			return "";
		}

		Gson gson = new Gson();

		// 获取父亲的FCB
		ByteBuffer fatherBf = this.diskManager.io.read(fcb.fatherBlockId, 1);
		String fatherFCBJson = new String(fatherBf.array(), Config.CHARSET);
		FCB fatherFCB = gson.fromJson(fatherFCBJson, FCB.class);

		return this.recursiveGetPath(fatherFCB) + "/" + fcb.filename;
	}

	/**
	 * 格式化，清空所有数据
	 */
	public void format() {
		this.diskManager.format();

		this.init();
	}

	/**
	 * 退出系统核心
	 */
	public void exit() {
		System.out.println("准备退出系统...");
		// 写回磁盘
		this.diskManager.update();
		System.out.println("退出");
	}

	/**
	 * 在当前目录下通过文件名找FCB
	 * 
	 * @param filename
	 *            文件名
	 * @return 找到的FCB
	 */
	public FCB getFCB(String filename, FILE_TYPE type) {
		for (int i = 0; i < this.currentDir.length; i++) {
			if (this.currentDir[i] == null) {
				break;
			}
			if (this.currentDir[i].filename.equals(filename)
					&& this.currentDir[i].type == type) {
				return this.currentDir[i];
			}
		}
		return null;
	}

	/**
	 * 更新所给的FCB
	 * 
	 * @param fcb
	 *            指定的FCB
	 */
	public void updateFCB(FCB fcb) {
		fcb.updatedDate = new Date();

		Gson gson = new Gson();

		this.diskManager.io.write(fcb.blockId, 1, gson.toJson(fcb));
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
			if (this.currentDir[i] == null) {
				break;
			}
			if (this.currentDir[i].filename.equals(filename)
					&& this.currentDir[i].type == type) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计算当前文件夹下的文件个数
	 */
	public void countFiles() {
		for (this.fileCount = 0; this.fileCount < this.currentDir.length; this.fileCount++) {
			if (this.currentDir[this.fileCount] == null) {
				break;
			}
		}
	}
}
