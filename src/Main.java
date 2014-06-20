import com.google.gson.Gson;

import controller.SystemCore;

/**
 * 
 * @author Tom Hu
 * 
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// MainController mainController = new MainController();
		// mainController.showMainView();

		Gson gson = new Gson();

		// String json = "[null,null,null]";
		//
		// ByteBuffer bf = ByteBuffer.wrap(json.getBytes(Config.CHARSET));
		//
		// FCB[] test = gson.fromJson(new String(bf.array(), Config.CHARSET),
		// FCB[].class);
		//
		// System.out.println(test);

		SystemCore core = new SystemCore();
		
		System.out.println(core.getCurrentPath());

//		core.createFile("dhe");
//
//		core.updateFile(core.getFCB("dhe", FILE_TYPE.FILE), "DSFADSFAS");
//
//		core.createDir("new folder");
		
		core.enterDir("new folder");
		
		System.out.println(core.getCurrentPath());

//		core.createFile("hdhdh");
//		
//		core.createDir("tom hu");
		
		core.enterDir("tom hu");
		
		System.out.println(core.getCurrentPath());

		core.leaveDir();
		
		System.out.println(core.getCurrentPath());

		core.exit();

	}

}
