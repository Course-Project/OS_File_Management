import com.google.gson.Gson;

import model.sys.Config;
import model.sys.FCB;
import controller.IO;




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

		FCB t = new FCB("fileneenenene", 0, Config.FILE_TYPE.DIRECTORY, 10);
		
		Gson gson = new Gson();
		
		String json = gson.toJson(t);
		
		IO io = new IO();
		io.init();
		io.write(0, 1, json);
		
		String o = io.read(0, 1);
		System.out.println(o.length());
		
		FCB u = gson.fromJson(o, FCB.class);
		
		System.out.println(u);
//		io.update();

	}

}
