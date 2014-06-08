import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		MainController mainController = new MainController();
//		mainController.showMainView();
//		
//		File fileA = new File("asdf");
//		fileA.save("qwerwqersdfas");
//		
//		Directory dirA = new Directory("new folder");
//		dirA.addDocument(fileA);
//		
//		File fileB = new File("qrewreqwt");
//		fileB.save("qewrewrqew\ndsfdsfewrq");
//		
//		dirA.addDocument(fileB);
//		
//		fileA.save("1231231231231231231212312312312");
//		
//		System.out.println(fileA.getSize());
//		System.out.println(fileB.getSize());
//		System.out.println(dirA.getSize());
		
		ByteBuffer b = ByteBuffer.allocate(8);
		ByteBuffer c = ByteBuffer.allocate(8);
		
		b.put("asrrdf".getBytes(Charset.forName("UTF-8")));
		
//		System.out.println(new String(b.array(), Charset.forName("UTF-8")));
		
		try {
			File file = new File("/Users/tomhu/Desktop/1.bin");
//			FileChannel fc = new FileOutputStream(file).getChannel();
//			fc.write(b);
//			fc.close();
			
			FileChannel fwc = new FileInputStream(file).getChannel();
			fwc.read(c);
			fwc.close();
			String d = new String(c.array(), Charset.forName("UTF-8"));
			System.out.println(d);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
