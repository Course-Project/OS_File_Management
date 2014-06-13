package controller;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Vector;

import model.sys.Block;
import model.sys.Config;

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

		// 写回物理磁盘
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
	 * 从Block中读取内容
	 * 
	 * @param startBlockId
	 *            起始的块的编号
	 * @param length
	 *            连续读入的块的个数
	 * @return 返回ByteBuffer，包含所需要的字符串内容
	 */
	public String read(int startBlockId, int length) {
		String result = "";
		for (int i = startBlockId; i < startBlockId + length; i++) {
			ByteBuffer targetBuffer = this.blocks.get(i).getBinData();
			byte[] target = new byte[targetBuffer.limit()];
			targetBuffer.get(target, 0, target.length);
			result += new String(target, Config.CHARSET);
		}
		return result;
	}

	/**
	 * 向Block中写入内容
	 * 
	 * @param startBlockId
	 *            起始的块的编号
	 * @param length
	 *            连续写入的块的个数
	 * @param content
	 *            写入的内容
	 */
	public void write(int startBlockId, int length, String content) {
		int i = 0;
		int offset = 0;
		int l = Math.min(Config.BLOCK_SIZE, content.length() - offset);
		while (l > 0 && i < startBlockId + length) {
			this.blocks.get(i).wipe(); // 清空该块，从头写起
			this.blocks.get(i).getBinData()
					.put(content.getBytes(Config.CHARSET), offset, l); // 写入数据
			this.blocks.get(i++).getBinData().flip(); // 修改limit，保证刚刚被写入的数据在恰好在有效范围内

			offset += l;
			l = Math.min(Config.BLOCK_SIZE, content.length() - offset);
		}

		// 内容大小小于写入的块，将之后的块清空
		while (i < startBlockId + length) {
			this.blocks.get(i++).wipe();
		}
	}
}
