package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import model.sys.Config.FILE_TYPE;
import model.sys.FCB;
import view.DocumentIconPanel;
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
		
		// 添加关闭事件监听
		this.view.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("窗口已关闭");
				
				// 系统核心退出
				MainController.this.systemCore.exit();
				
				// 关闭窗口
				super.windowClosed(e);
			}

		});

		this.configureContentPanel();
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
					// model进入文件夹
					MainController.this.systemCore.enterDir(d.getFilename());

					// 重绘view
					MainController.this.refreshView();

					// 重新添加监听
					MainController.this.addListenerForEachDocumentIcon();

				} else {
					// 双击文件
					// 获取文件FCB
					FCB fcb = MainController.this.systemCore.getFCB(
							d.getFilename(), FILE_TYPE.FILE);

					// 弹出Edit View，根据FCB加载

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
				filename = (String) JOptionPane.showInputDialog(null,
						"文件夹名不允许为空！\n请输入文件夹名称:", "新建文件夹",
						JOptionPane.WARNING_MESSAGE);
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
	 * 根据当前FCB对应的目录文件刷新View
	 */
	private void refreshView() {
		this.view.reloadContent(this.systemCore.currentDir);
	}

	/**
	 * 为每个图标添加监听
	 */
	private void addListenerForEachDocumentIcon() {
		this.view
				.addDocumentIconPanelMouseListener(this.documentIconPanelMouseListener);
	}
}
