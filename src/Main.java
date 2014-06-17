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
		
		SystemCore core = new SystemCore();
		
		core.createFile("dhe");
		
		System.out.println(core.currentDirFCB);
		
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(core.currentDir[0]));

	}

}
