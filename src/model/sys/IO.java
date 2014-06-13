package model.sys;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Vector;

public class IO {

	private Vector<Block> blocks = new Vector<Block>();

	public IO() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 初始化所有block，从磁盘同步内容
	 */
	public void init() {
		System.out.println("系统开始初始化");
		File rootDir = new File("disk");
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}

		for (int i = 0; i < Config.BLOCK_COUNT; i++) {
			Block block = new Block(i);
			this.blocks.add(block);
			block.sync();
		}
		
		this.update();
		System.out.println("系统初始化完成");
	}
	
	/**
	 * 将所有块的数据写回物理磁盘
	 */
	public void update() {
		System.out.println("系统数据写回物理磁盘");
		for (int i = 0; i < Config.BLOCK_COUNT; i++) {
			this.blocks.get(i).update();
		}
		System.out.println("系统数据保存完毕");
	}

	/**
	 * 读取数据块内容
	 * @param blockId
	 * @return
	 */
	public ByteBuffer read(int blockId) {
		return this.blocks.get(blockId).getBinData();
	}
}
