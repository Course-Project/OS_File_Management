package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import com.google.gson.Gson;

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

	/**
	 * 显示编辑界面
	 * 
	 * @param fcb
	 *            需要编辑的文件的FCB
	 */
	private void showEditView(FCB fcb) {
		// 弹出Edit View，根据FCB加载
		EditView editView = new EditView(fcb,
				MainController.this.systemCore.readFile(fcb));

		// 为Edit Window添加监听
		editView.addWindowListener(MainController.this.editWindowListener);

		// 显示Edit View
		editView.setVisible(true);
	}

	// Listener
	/**
	 * 主界面内容面板右键点击监听
	 */
	private MouseListener rightClickListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Deselect documents
			if (e.getButton() == MouseEvent.BUTTON1) {
				MainController.this.view.deselectDocuments();
			}

			// Popup menu
			if (e.getButton() == MouseEvent.BUTTON3) {
				boolean isRoot = (MainController.this.systemCore.currentDirFCB.fatherBlockId == -1);

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

				if (isRoot) {
					menu.addSeparator();

					JMenuItem formatMenu = new JMenuItem("Format");
					formatMenu
							.addActionListener(MainController.this.formatMenuActionListener);
					menu.add(formatMenu);
				}

				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

	};

	/**
	 * 图标点击监听，包括单击选中，双击打开，右键弹出菜单
	 */
	private MouseListener documentIconPanelMouseListener = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			// Select current document
			MainController.this.view.deselectDocuments();
			DocumentIconPanel d = (DocumentIconPanel) e.getComponent();
			d.setSelected(true);

			// 获取文件FCB
			FCB fcb = MainController.this.systemCore.getFCB(d.getFilename(),
					d.getType());

			// Double click
			if (e.getClickCount() == 2) {
				// 判断双击的类型
				if (d.getType() == FILE_TYPE.DIRECTORY) {
					// 双击文件夹
					// model进入下一级文件夹
					MainController.this.systemCore.enterDir(d.getFilename());

					// 重绘view
					MainController.this.refreshView();

				} else {
					// 双击文件
					// 显示编辑窗口
					MainController.this.showEditView(fcb);
				}

				System.out.println("Double Click");
			}

			// Right click
			if (e.getButton() == MouseEvent.BUTTON3) {
				JPopupMenu documentMenu = new JPopupMenu();
				JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
				MainController.this.openMenuActionListener.fcb = fcb;
				openMenuItem
						.addActionListener(MainController.this.openMenuActionListener);
				documentMenu.add(openMenuItem);

				if (d.getType() == FILE_TYPE.FILE) {
					JMenuItem editMenuItem = new JMenuItem("Edit",
							KeyEvent.VK_E);
					MainController.this.editMenuActionListener.fcb = fcb;
					editMenuItem
							.addActionListener(MainController.this.editMenuActionListener);
					documentMenu.add(editMenuItem);
				}

				JMenuItem renameMenuItem = new JMenuItem("Rename",
						KeyEvent.VK_R);
				MainController.this.renameMenuActionListener.fcb = fcb;
				renameMenuItem
						.addActionListener(MainController.this.renameMenuActionListener);
				documentMenu.add(renameMenuItem);

				documentMenu.addSeparator();

				JMenuItem getInfoMenuItem = new JMenuItem("Get info",
						KeyEvent.VK_I);
				MainController.this.getInfoMenuActionListener.fcb = fcb;
				getInfoMenuItem
						.addActionListener(MainController.this.getInfoMenuActionListener);
				documentMenu.add(getInfoMenuItem);

				documentMenu.addSeparator();

				JMenuItem deleteMenuItem = new JMenuItem("Delete",
						KeyEvent.VK_D);
				MainController.this.deleteMenuActionListener.fcb = fcb;
				deleteMenuItem
						.addActionListener(MainController.this.deleteMenuActionListener);
				documentMenu.add(deleteMenuItem);

				documentMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

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
					MainController.this.view,
					"Please enter your new file's name:", "New file",
					JOptionPane.INFORMATION_MESSAGE);

			if (filename == null) {
				// 用户取消
				return;
			}

			// 不允许文件名为空
			while (filename.equals("")) {
				filename = (String) JOptionPane
						.showInputDialog(
								MainController.this.view,
								"Filename cannot be empty! \nPlease enter your new file's name:",
								"New file", JOptionPane.WARNING_MESSAGE);

				if (filename == null) {
					// 用户取消
					return;
				}
			}

			if (MainController.this.systemCore.createFile(filename)) {
				// 添加到model成功，即创建文件成功
				// 添加到view
				DocumentIconPanel d = new DocumentIconPanel(FILE_TYPE.FILE,
						filename);
				d.addMouseListener(MainController.this.documentIconPanelMouseListener);
				MainController.this.view.addDocument(d);
			} else {
				// 创建文件失败
				// 可能是有重名，也可能空间不够
				// 弹出错误信息框
				JOptionPane
						.showMessageDialog(
								MainController.this.view,
								"Two possible reasons:\n1. The name \""
										+ filename
										+ "\" is already taken. Please choose a different name.\n2. Not enough disk space available. Delete some files.",
								"New file error", JOptionPane.ERROR_MESSAGE);
			}

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
					MainController.this.view,
					"Please enter your new folder's name:", "New folder",
					JOptionPane.INFORMATION_MESSAGE);

			if (filename == null) {
				// 用户取消
				return;
			}

			// 不允许文件名为空
			while (filename.equals("")) {
				filename = (String) JOptionPane
						.showInputDialog(
								MainController.this.view,
								"Folder's name cannot be empty! \nPlease enter your new folder's name:",
								"New folder", JOptionPane.WARNING_MESSAGE);

				if (filename == null) {
					// 用户取消
					return;
				}
			}

			if (MainController.this.systemCore.createDir(filename)) {
				// 添加到model成功，即创建文件夹成功
				// 添加到view
				DocumentIconPanel d = new DocumentIconPanel(
						FILE_TYPE.DIRECTORY, filename);
				d.addMouseListener(MainController.this.documentIconPanelMouseListener);
				MainController.this.view.addDocument(d);
			} else {
				// 创建文件夹失败
				// 可能是有重名，也可能空间不够
				// 弹出错误信息框
				JOptionPane
						.showMessageDialog(
								MainController.this.view,
								"Two possible reasons:\n1. The name \""
										+ filename
										+ "\" is already taken. Please choose a different name.\n2. Not enough disk space available. Delete some files.",
								"New folder error", JOptionPane.ERROR_MESSAGE);
			}

		}

	};

	private class CustomActionListener implements ActionListener {
		public FCB fcb = null;

		@Override
		public void actionPerformed(ActionEvent e) {

		}
	}

	/**
	 * "编辑"按钮按键监听
	 */
	private CustomActionListener editMenuActionListener = new CustomActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			MainController.this.showEditView(this.fcb);
		}

	};

	/**
	 * "打开"按钮按键监听
	 */
	private CustomActionListener openMenuActionListener = new CustomActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (this.fcb.type == FILE_TYPE.DIRECTORY) {
				// model进入下一级文件夹
				MainController.this.systemCore.enterDir(this.fcb.filename);

				// 重绘view
				MainController.this.refreshView();
			} else {
				// 显示编辑窗口
				MainController.this.showEditView(fcb);
			}
		}

	};

	/**
	 * "删除"按钮按键监听
	 */
	private CustomActionListener deleteMenuActionListener = new CustomActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 获取用户的选择
			int result = JOptionPane.showConfirmDialog(
					MainController.this.view,
					"Are you sure you want to permanently delete this item?",
					"Delete", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (result == 0) {
				// 确定删除
				if (this.fcb.type == FILE_TYPE.DIRECTORY) {
					// model删除文件夹
					MainController.this.systemCore.deleteDir(this.fcb.filename);
				} else {
					// model删除文件
					MainController.this.systemCore
							.deleteFile(this.fcb.filename);
				}

				// 重绘view
				MainController.this.refreshView();
			}
		}

	};

	/**
	 * "重命名"按钮按键监听
	 */
	private CustomActionListener renameMenuActionListener = new CustomActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 获取文件名
			String filename = (String) JOptionPane.showInputDialog(
					MainController.this.view, "Folder name:", "Rename",
					JOptionPane.INFORMATION_MESSAGE, null, null,
					this.fcb.filename);

			if (filename == null) {
				// 用户取消
				return;
			}

			// 不允许文件名为空
			while (filename.equals("")) {
				filename = (String) JOptionPane.showInputDialog(
						MainController.this.view, "Folder name:", "Rename",
						JOptionPane.INFORMATION_MESSAGE, null, null,
						this.fcb.filename);
			}

			this.fcb.filename = filename;

			Gson gson = new Gson();

			// 更新文件FCB
			MainController.this.systemCore.updateFCB(this.fcb);

			// 更新当前目录的目录文件
			MainController.this.systemCore.updateFile(
					MainController.this.systemCore.currentDirFCB,
					gson.toJson(MainController.this.systemCore.currentDir));

			// 刷新界面
			MainController.this.refreshView();
		}

	};

	/**
	 * "格式化"按钮按键监听
	 */
	private ActionListener formatMenuActionListener = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 获取用户的选择
			int result = JOptionPane
					.showConfirmDialog(
							MainController.this.view,
							"All the data will be erased from the disk.\nAre you sure to FORMAT disk?",
							"FORMAT!!", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);

			if (result == 0) {
				// 确定格式化
				MainController.this.systemCore.format();
			}

			// 刷新界面
			MainController.this.refreshView();
		}

	};

	/**
	 * "属性"按钮按键监听
	 */
	private CustomActionListener getInfoMenuActionListener = new CustomActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// 弹出详细信息框
			JOptionPane.showMessageDialog(MainController.this.view,
					MainController.this.systemCore.getFileInfo(this.fcb),
					"Info", JOptionPane.PLAIN_MESSAGE);
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

		}

		@Override
		public void windowClosing(WindowEvent e) {

		}

		@Override
		public void windowClosed(WindowEvent e) {
			System.out.println("窗口已关闭");

			// 系统核心退出
			MainController.this.systemCore.exit();
		}

		@Override
		public void windowIconified(WindowEvent e) {

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

		}

	};

	/**
	 * 编辑窗口的监听
	 */
	private WindowListener editWindowListener = new WindowListener() {

		@Override
		public void windowOpened(WindowEvent e) {

		}

		@Override
		public void windowClosing(WindowEvent e) {

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

		}

		@Override
		public void windowDeiconified(WindowEvent e) {

		}

		@Override
		public void windowActivated(WindowEvent e) {

		}

		@Override
		public void windowDeactivated(WindowEvent e) {

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
