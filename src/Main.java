import controller.MainController;

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
		MainController mainController = new MainController();
		mainController.showMainView();
//
//		String s = (String) JOptionPane.showInputDialog(null, "请输入文件夹名称:", "新建文件夹", JOptionPane.INFORMATION_MESSAGE);
//		
//		while (s == null || s.equals("")) {
//			s = (String) JOptionPane.showInputDialog(null, "文件夹名不允许为空！\n请输入文件夹名称:", "新建文件夹", JOptionPane.WARNING_MESSAGE);
//		}
//		
//		System.out.println(s);
	}
}
