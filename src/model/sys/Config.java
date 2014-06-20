package model.sys;

import java.nio.charset.Charset;

/**
 * 
 * @author Tom Hu
 * 
 */

public class Config {

	public static final int BLOCK_SIZE = 512;
	public static final int BLOCK_COUNT = 8000;

	public static final int SYS_BLOCK_COUNT = 256;

	public static final int FILE_MAX_BLOCKS = 10;

	public static enum FILE_TYPE {
		FILE, DIRECTORY
	};

	public static final Charset CHARSET = Charset.forName("UTF-8");
}
