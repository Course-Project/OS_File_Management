package model.sys;

import java.io.File;
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
		this.id = id;
		this.binData = ByteBuffer.allocate(Config.BLOCK_SIZE);
		this.filePath = "disk/" + this.id + ".bin";
	}

	/**
	 * 将该块内容清空
	 */
	public void wipe() {
		this.binData.clear(); //并未真正清空数据，只是重置了标记
	}

	/**
	 * 从物理磁盘上读入该块的内容，存入binData
	 */
	public void sync() {
		File binFile = new File(this.filePath);
		if (!binFile.exists()) {
//			System.out.println("Warning: \"" + this.filePath + "\" not found");
			return;
		}
		
		FileChannel inputChannel;
		try {
			inputChannel = new FileInputStream(binFile).getChannel();
			inputChannel.read(this.binData);
			this.binData.flip();
			inputChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将binData数据写回物理磁盘
	 */
	public void update() {
		File binFile = new File(this.filePath);
		
		FileChannel outputChannel;
		try {
			outputChannel = new FileOutputStream(binFile).getChannel();
			this.binData.rewind();
			outputChannel.write(this.binData);
			this.binData.flip();
			outputChannel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前块数据
	 */
	public ByteBuffer getBinData() {
		return this.binData;
	}
}
