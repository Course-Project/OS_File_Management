package model.sys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author Tom Hu
 * 
 */
public class Block {

	// Core data
	private int id; // id为该块的编号
	private ByteBuffer binData; // binData为该块存储内容的buffer
	private String filePath; // filePath为该块在物理磁盘上*.bin文件的存储路径

	/**
	 * 构造函数
	 * 
	 * @param id
	 */
	public Block(int id) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.binData = ByteBuffer.allocate(Config.BLOCK_SIZE);
		this.filePath = "disk/" + this.id + ".bin";
	}

	/**
	 * 将该块内容清空
	 */
	public void wipe() {
		this.binData.clear();
	}

	/**
	 * 从物理磁盘上读入该块的内容，存入binData
	 */
	public void sync() {
		FileChannel inputChannel;
		try {
			inputChannel = new FileInputStream(this.filePath).getChannel();
			inputChannel.read(this.binData);
			inputChannel.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 将binData数据写回物理磁盘
	 */
	public void update() {
		FileChannel outputChannel;
		try {
			outputChannel = new FileOutputStream(this.filePath).getChannel();
			this.binData.flip();
			outputChannel.write(this.binData);
			outputChannel.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
