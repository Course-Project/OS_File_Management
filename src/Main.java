
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

		byte test = 0;
		test |= (1 << 7);
		test |= (1 << 0);
		
		System.out.println(test);
		
		test &= (~(1 << 7));
		
		System.out.println(test);

	}

}
