package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import model.sys.Config.FILE_TYPE;
import model.sys.FCB;
import view.DocumentIconPanel;
import view.EditView;
import view.MainView;

/**
 * 
 * @author Tom Hu
 * 
 */
public class MainController {

	private MainView view;

	private SystemCore systemCore;

	/**
	 * 构造函数
	 */
	public MainController() {
		super();

		// Initialize systemCore
		this.systemCore = new SystemCore();

		// UI Methods
		this.configureMainView();
	}

	// UI Methods
	/**
	 * 初始化主界面
	 */
	private void configureMainView() {
		// 根据当前目录文件初始化view
		this.view = new MainView(this.systemCore.currentDir);

		// 添加右键监听
		this.view.addRightClickListener(this.rightClickListener);

		// 添加后退按钮监听
		this.view.addBackButtonActionListener(this.backButtonActionListener);

		// 添加前往按钮监听
		this.view.addGoButtonActionListener(this.goButtonActionListener);

		// 添加关闭事件监听
		this.view.addWindowListener(this.mainWindowListener);

		this.configureContentPanel();

		// 显示当前路径
		this.view.addressTextField.setText(this.systemCore.getCurrentPath());
	}

	/**
	 * 初始化内容面板
	 */
	private void configureContentPanel() {
		// 为每个图标添加监听
		this.addListenerForEachDocumentIcon();
	}

	/**
	 * 显示主界面
	 */
	public void showMainView() {
		this.view.showView();
	}

	// Listener
	/**
	 * 主界面内容面板右键点击监听
	 */
	private MouseListener rightClickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Deselect documents
			if (e.getButton() == MouseEvent.BUTTON1) {
				MainController.this.view.deselectDocuments();
			}

			// Popup menu
			if (e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu menu = new JPopupMenu();
				JMenuItem newFileMenu = new JMenuItem("New File", KeyEvent.VK_N);
				newFileMenu.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_N, ActionEvent.CTRL_MASK));
				newFileMenu
						.addActionListener(MainController.this.newFileActionListener);
				menu.add(newFileMenu);

				JMenuItem newFolderMenu = new JMenuItem("New Folder",
						KeyEvent.VK_F);
				newFolderMenu.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_F, ActionEvent.CTRL_MASK));
				newFolderMenu
						.addActionListener(MainController.this.newFolderActionListener);
				menu.add(newFolderMenu);

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 图标点击监听，包括单击选中，双击打开，右键弹出菜单
	 */
	private MouseListener documentIconPanelMouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			// Select current document
			MainController.this.view.deselectDocuments();
			((DocumentIconPanel) e.getComponent()).setSelected(true);

			// Double click
			if (e.getClickCount() == 2) {
				DocumentIconPanel d = (DocumentIconPanel) e.getComponent();

				// 判断双击的类型
				if (d.getType() == FILE_TYPE.DIRECTORY) {
					// 双击文件夹
					// model进入下一级文件夹
					MainController.this.systemCore.enterDir(d.getFilename());

					// 重绘view
					MainController.this.refreshView();

				} else {
					// 双击文件
					// 获取文件FCB
					FCB fcb = MainController.this.systemCore.getFCB(
							d.getFilename(), FILE_TYPE.FILE);

					// 弹出Edit View，根据FCB加载
					EditView editView = new EditView(fcb,
							MainController.this.systemCore.readFile(fcb));

					// 为Edit Window添加监听
					editView.addWindowListener(MainController.this.editWindowListener);

					// 显示Edit View
					editView.setVisible(true);
				}

				System.out.println("Double Click");
			}

			// Right click
			if (e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu documentMenu = new JPopupMenu();
				JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
				documentMenu.add(openMenuItem);

				JMenuItem editMenuItem = new JMenuItem("Edit", KeyEvent.VK_E);
				documentMenu.add(editMenuItem);

				JMenuItem renameMenuItem = new JMenuItem("Rename",
						KeyEvent.VK_R);
				documentMenu.add(renameMenuItem);

				documentMenu.addSeparator();

				JMenuItem getInfoMenuItem = new JMenuItem("Get info",
						KeyEvent.VK_I);
				documentMenu.add(getInfoMenuItem);

				documentMenu.addSeparator();

				JMenuItem deleteAll = new JMenuItem("Delete", KeyEvent.VK_D);
				documentMenu.add(deleteAll);

				documentMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 监听新建文件的按钮
	 */
	private ActionListener newFileActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 新建文件
			// 获取文件名
			String filename = (String) JOptionPane.showInputDialog(
					MainController.this.view, "请输入文件夹名称:", "新建文件夹",
					JOptionPane.INFORMATION_MESSAGE);

			// 不允许文件名为空
			while (filename == null || filename.equals("")) {
				filename = (String) JOptionPane.showInputDialog(null,
						"文件夹名不允许为空！\n请输入文件夹名称:", "新建文件夹",
						JOptionPane.WARNING_MESSAGE);
			}

			// 添加到model
			MainController.this.systemCore.createFile(filename);

			// 添加到view
			DocumentIconPanel d = new DocumentIconPanel(FILE_TYPE.FILE,
					filename);
			d.addMouseListener(MainController.this.documentIconPanelMouseListener);
			MainController.this.view.addDocument(d);
		}
	};

	/**
	 * 监听新建文件夹的按钮
	 */
	private ActionListener newFolderActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 新建文件夹
			// 获取文件名
			String filename = (String) JOptionPane.showInputDialog(
					MainController.this.view, "请输入文件夹名称:", "新建文件夹",
					JOptionPane.INFORMATION_MESSAGE);

			// 不允许文件名为空
			while (filename == null || filename.equals("")) {
				filename = (String) JOptionPane.showInputDialog(
						MainController.this.view, "文件夹名不允许为空！\n请输入文件夹名称:",
						"新建文件夹", JOptionPane.WARNING_MESSAGE);
			}

			// 添加到model
			MainController.this.systemCore.createDir(filename);

			// 添加到view
			DocumentIconPanel d = new DocumentIconPanel(FILE_TYPE.DIRECTORY,
					filename);
			d.addMouseListener(MainController.this.documentIconPanelMouseListener);
			MainController.this.view.addDocument(d);
		}

	};

	/**
	 * "后退"按钮按键监听
	 */
	private ActionListener backButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 点击后退按钮

			if (MainController.this.systemCore.leaveDir()) {
				// 确认回到上一级目录
				// 重绘view
				MainController.this.refreshView();
			} else {
				// 根目录
				JOptionPane.showMessageDialog(MainController.this.view,
						"Already in root directory", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}

	};

	/**
	 * "前往"按钮按键监听
	 */
	private ActionListener goButtonActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 获取地址栏地址
			String path = MainController.this.view.addressTextField.getText();

			if (path.charAt(0) != '/') {
				// 路径非法
				JOptionPane.showMessageDialog(MainController.this.view,
						"Directory doesn't exist!", "Warning",
						JOptionPane.WARNING_MESSAGE);

				return;
			}

			// 获取当前地址
			String currentPath = MainController.this.systemCore
					.getCurrentPath();

			// 拆分
			String[] pathArray = path.split("/");
			String[] currentPathArray = currentPath.split("/");

			if (pathArray.length == 0) {
				pathArray = new String[1];
				pathArray[0] = "";
			}

			if (currentPathArray.length == 0) {
				currentPathArray = new String[1];
				currentPathArray[0] = "";
			}

			// 对比
			int length = Math.min(pathArray.length, currentPathArray.length);
			int i = 0;
			for (i = 0; i < length; i++) {
				if (!pathArray[i].equals(currentPathArray[i])) {
					break;
				}
			}

			if (pathArray.length == currentPathArray.length && i == length) {
				// 两拆分后的数组相同，即路径没变化，无需继续下面的步骤
				return;
			}

			// 计算
			// 向后退的步数
			int stepOut = currentPathArray.length - i;
			// 向前进的步数
			int stepIn = pathArray.length - i;

			// 临时保存当前目录FCB和目录文件
			FCB fcb = MainController.this.systemCore.currentDirFCB;
			FCB[] dir = MainController.this.systemCore.currentDir;

			// 开始后退再前进
			boolean success = true;
			// 后退
			for (int j = 0; j < stepOut; j++) {
				MainController.this.systemCore.leaveDir();
			}

			// 前进
			for (int j = 0; j < stepIn; j++) {
				if (!MainController.this.systemCore.enterDir(pathArray[i++])) {
					success = false;
					break;
				}
			}

			if (success) {
				// 成功跳转
				// 刷新界面
				MainController.this.refreshView();
			} else {
				// 目录不存在
				JOptionPane.showMessageDialog(MainController.this.view,
						"Directory doesn't exist!", "Warning",
						JOptionPane.WARNING_MESSAGE);

				// 恢复
				MainController.this.systemCore.currentDirFCB = fcb;
				MainController.this.systemCore.currentDir = dir;
				MainController.this.systemCore.countFiles();
			}

		}

	};

	/**
	 * 主窗口的监听，主要是关闭事件
	 */
	private WindowListener mainWindowListener = new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosed(WindowEvent e) {
			System.out.println("窗口已关闭");

			// 系统核心退出
			MainController.this.systemCore.exit();
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 编辑窗口的监听
	 */
	private WindowListener editWindowListener = new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub

			EditView editView = (EditView) e.getComponent();
			
			if (!editView.edited) {
				// 文本没有变化
				return;
			}

			// 获取用户的选择
			int result = JOptionPane.showConfirmDialog(editView,
					"Would you like to SAVE before leaving?", "Exit",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (result == 0) {
				// 退出并保存
				editView.saveOnExit = true;
				editView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else if (result == 1) {
				// 退出不保存
				editView.saveOnExit = false;
				editView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			} else {
				// 取消
				editView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
		}

		@Override
		public void windowClosed(WindowEvent e) {
			System.out.println("编辑窗口关闭");

			EditView editView = (EditView) e.getComponent();

			if (editView.edited && editView.saveOnExit) {
				// 保存文件
				MainController.this.systemCore.updateFile(
						editView.getDataFCB(), editView.getContent());
			}
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	};

	/**
	 * 根据当前FCB对应的目录文件刷新View
	 */
	private void refreshView() {
		this.view.reloadContent(this.systemCore.currentDir);

		// 重新添加监听
		this.addListenerForEachDocumentIcon();

		// 更新当前路径
		this.view.addressTextField.setText(MainController.this.systemCore
				.getCurrentPath());
	}

	/**
	 * 为每个图标添加监听
	 */
	private void addListenerForEachDocumentIcon() {
		this.view
				.addDocumentIconPanelMouseListener(this.documentIconPanelMouseListener);
	}
}
