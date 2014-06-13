import java.util.Vector;

import com.google.gson.Gson;

import model.sys.FCB;
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
		
		FCB t = new FCB(null, 0, null, 0, 0, 0);
		
		FCB[] test = new FCB[10];
		test[0] = t;
		
		Gson gson = new Gson();
		
		String json = gson.toJson(test);
		
		FCB[] d = gson.fromJson(json, FCB[].class);
		
		System.out.println(d[0]);

	}

}
