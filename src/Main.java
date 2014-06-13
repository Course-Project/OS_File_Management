import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import model.sys.Config;
import model.sys.FCB;
import model.sys.IO;


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

		FCB test = new FCB("testtesdsfdsfsdfsadt", 0, Config.FILE_TYPE.FILE, 3);
		
		System.out.println("Address: " + test.address);
        System.out.println("Filename: " + test.filename);
        System.out.println("Size: " + test.size);
        System.out.println("Created: " + test.createdDate);
        System.out.println("Updated: " + test.updatedDate);
		
		byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(test);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(bytes);
        
        ByteBuffer bb = ByteBuffer.allocate(512);
        bb.put(bytes);
        
        System.out.println("ByteBuffer: " + bb);
        
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            bis.close();
            ois.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println(obj.getClass());
        
        FCB o = (FCB) obj;
        
        System.out.println("Address: " + o.address);
        System.out.println("Filename: " + o.filename);
        System.out.println("Size: " + o.size);
        System.out.println("Created: " + o.createdDate);
        System.out.println("Updated: " + o.updatedDate);

        IO io = new IO();
        io.init();

	}

}
