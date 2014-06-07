import model.document.Directory;
import model.document.File;
import controller.MainController;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainController mainController = new MainController();
		mainController.showMainView();
		
		File fileA = new File("asdf");
		fileA.save("qwerwqersdfas");
		
		Directory dirA = new Directory("new folder");
		dirA.addDocument(fileA);
		
		File fileB = new File("qrewreqwt");
		fileB.save("qewrewrqew\ndsfdsfewrq");
		
		dirA.addDocument(fileB);
		
		fileA.save("1231231231231231231212312312312");
		
		System.out.println(fileA.getSize());
		System.out.println(fileB.getSize());
		System.out.println(dirA.getSize());
	}

}
