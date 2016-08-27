package controller;

import java.nio.ByteBuffer;

import model.sys.Config;
import model.sys.FCB;

public class DiskManager {
	
	private byte[] bitMap;
	public IO io;
	public boolean online;

	/**
	 * 构造函数
	 * 为bitMap和io赋初值
	 */
	public DiskManager() {
		super();
		this.bitMap = new byte[Config.BLOCK_COUNT / 8];
		this.io = new IO();
		this.online = false;
	}
	
	/**
	 * 初始化
	 */
	public void init() {
		if (this.online) {
			return;
		}
		
		// 初始化io
		this.io.init();
		
		// 从Block中读取位图
		this.bitMap = this.io.read(0, 2).array();
		
		this.online = true;
	}
	
	/**
	 * 格式化磁盘
	 */
	public void format() {
		this.online = false;
		this.io.online = false;
		for (int i = 0; i < Config.BLOCK_COUNT; i++) {
			this.io.write(i, 1, "");
		}
		
		// 重新初始化位图以及根目录
		this.io.initRootFile();
	}
	
	/**
	 * 更新位图，存入Block
	 */
	public void updateBitMap() {
		this.io.write(0, this.bitMap.length / 512 + 1, this.bitMap);
	}
	
	/**
	 * 申请连续length个块
	 * @param startBlockId 申请块的起始id
	 * @param length 申请的块的个数
	 */
	public void alloc(int startBlockId, int length) {
		int max = Math.min(startBlockId + length, this.bitMap.length);
		for (int i = 0; i < max; i++) {
			this.bitMap[i / 8] |= (1 << (i % 8));
		}
		this.updateBitMap();
	}
	
	/**
	 * 连续释放length个块
	 * @param startBlockId 释放块的起始id
	 * @param length 释放的块的个数
	 */
	public void free(int startBlockId, int length) {
		int max = Math.min(startBlockId + length, this.bitMap.length);
		for (int i = 0; i < max; i++) {
			this.bitMap[i / 8] &= (~(1 << (i % 8)));
		}
		this.updateBitMap();
	}
	
	/**
	 * 所有数据写回磁盘
	 */
	public void update() {
		this.io.update();
	}
	
	/**
	 * 搜索连续长度为length的空闲块
	 * @param length 所请求的空闲块链的长度
	 * @return 空闲块链起点
	 */
	public int getFreeSpace(int length) {
		return this.getFreeSpace(length, 256);
	}
	
	/**
	 * 从startBlockId开始，搜索连续长度为length的空闲块链
	 * @param length 所请求的空闲块链的长度
	 * @param startBlockId 搜索的起点
	 * @return 空闲块起点
	 */
	public int getFreeSpace(int length, int startBlockId) {
		startBlockId = Math.max(Config.SYS_BLOCK_COUNT, startBlockId);
		int max = this.bitMap.length;
		
		for (int i = startBlockId; i < max; ) {
			int j = i;
			while (j < max && (this.bitMap[j / 8] & (byte) (1 << (j % 8))) == 0) {
				j++;
			}
			if (j - i >= length) {
				return i;
			}
			i = j + 1;
		}
		return 0;
	}
	
	/**
	 * 根据FCB来获取文件内容
	 * @param fcb
	 * @return
	 */
	public String readFile(FCB fcb) {
		ByteBuffer resultBuffer = this.io.read(fcb.dataStartBlockId, fcb.size);
		return new String(resultBuffer.array(), Config.CHARSET);
	}

}
