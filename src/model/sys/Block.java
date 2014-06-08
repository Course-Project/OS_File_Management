package model.sys;

import java.nio.ByteBuffer;

public class Block {

	// Core data
	private int id;
	private ByteBuffer binData;

	// Constructor

	public Block(int id) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.binData = ByteBuffer.allocate(Config.BLOCK_SIZE);
	}

	public void wipe() {
		this.binData.clear();
	}

	public void sync() {

	}

	public void update() {
		
	}

}
