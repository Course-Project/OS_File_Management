package controller;

import model.sys.Config;

public class DiskManager {
	
	private byte[] bitMap;
	private IO io;
	private boolean online;

	/**
	 * 构造函数
	 * 为bitMap和io赋初值
	 */
	public DiskManager() {
		super();
		// TODO Auto-generated constructor stub
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
	
	

}
