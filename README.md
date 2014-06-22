OS File Management
=================

同济大学软件学院2012级，胡圣托(1252960)

程序打开时会有点慢，因为需要初始化并读取数据，请耐心等待

另外，本项目中对中文支持不太好，在新建文件、新建文件夹、编辑文件的时候尽量使用英文

Github地址：[h1994st](https://github.com/h1994st)/[OS_File_Management](https://github.com/h1994st/OS_File_Management)

## 目录

- 项目简介
- 界面简介
- 项目结构
- 实现思想
- 改进之处
- 代码文档

## 项目简介

- 项目名称：

	文件系统模拟程序

- 开发环境：

	Eclipse + JDK SE 7 + Mac OS X 10.9.3

- 项目概述：

	本项目模拟了一个文件系统，用户可以做到新建文件、新建文件夹、编辑文件、删除文件、删除文件夹、格式化磁盘等操作，就像平时系统中真正的文件管理器一样，只是本项目将一些功能简化了，当用户退出时，所有的信息将会被保存在电脑真实的磁盘上，下次打开时重新读入

## 界面简介

整体界面借鉴了_[邱峰学长的项目](https://github.com/VioletHill/OS_FileSystem)_的界面

- 主界面

	1. 主界面，根目录
	
		![Main](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/main.png)

	2. 右键点击弹出菜单
	
		![Main](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/main%20-%20popup%20menu.png)

	3. 新建文件时，输入文件名
	
		![New file](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/main%20-%20new%20file.png)

	4. 新建文件夹时出错
	
		![New folder error](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/main%20-%20new%20folder%20error.png)

- 编辑界面

	1. 双击打开某文件，弹出编辑界面
	
		![Edit](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/edit.png)

	2. 关闭编辑界面时，如果有更改过文件内容，弹出对话框询问是否保存
	
		![Edit](https://raw.githubusercontent.com/h1994st/OS_File_Managment/master/screenshot/edit%20-%20before%20exiting.png)

## 项目结构

本项目依照MVC的思想进行架构，各层次分明

```
src/
├── controller/
│   ├── DiskManager.java
│   ├── IO.java
│   ├── MainController.java
│   └── SystemCore.java
│
├── model/
│   └── sys/
│       ├── Block.java
│       ├── Config.java
│       └── FCB.java
│
└── view/
    ├── Config.java
    ├── DocumentIconPanel.java
    ├── EditView.java
    └── MainView.java
```

#### View

这一部分关乎整个项目的用户界面

1. Config.java

	该文件与界面无关，主要定义了一些整个View中用到的一些公共的参数，比如窗口长宽、文件图标的大小等

	```
	package view;

	public class Config {

		// Window
		public static final int WINDOW_WIDTH = 800;
		public static final int WINDOW_HEIGHT = 600;

		// File icon
		public static final int FILE_ICON_SIZE = 80;
		public static final int FILE_ICON_PANEL_SIZE = 100;

	}
	```

2. DocumentIconPanel.java

	该文件与文件的图标有关，添加了鼠标点击事件的监听，支持单击选中、双击打开、右键弹出菜单

3. EditView.java

	该文件与文件的编辑界面有关，可以编辑文字
	
	如果对文件进行过修改，窗口标题中的 ___"Edit"___ 会变成 ___"Edited"___ ，并且在退出时会询问用户是否需要保存；如果没有修改则直接退出

4. MainView.java

	该文件与程序主界面有关
	
	主界面上部分由 ___后退(Back)___ 、 ___前往(Go)___ 、 ___地址栏___ 组成
	
	下部分为主要部分，展示当前目录下的文件和文件夹，添加了鼠标点击事件的监听，右键弹出菜单，可以选择 ___"新建文件"___ 、 ___"新建文件夹"___ 、 ___"格式化"(仅根目录下)___

#### Model

下文叙述时“磁盘”指本程序中的“虚拟磁盘”，“物理块”指“虚拟磁盘中的块”；当提到“真实物理磁盘”时，才是指用户电脑的“物理磁盘”磁盘中“物理块”

1. Config.java

	该文件同样定义了一些整个Model中用到的一些公共参数

	```
	package model.sys;

	import java.nio.charset.Charset;

	public class Config {
		
		public static final int BLOCK_SIZE = 512;
		public static final int BLOCK_COUNT = 8000;

		public static final int SYS_BLOCK_COUNT = 256;

		public static final int FILE_MAX_BLOCKS = 10;

		public static enum FILE_TYPE {
			FILE, DIRECTORY
		};

		public static final Charset CHARSET = Charset.forName("UTF-8");		
	}
	```

2. Block.java

	该文件描述了磁盘中“物理块”的模型，它可以从真实物理磁盘中同步数据

3. FCB.java

	该文件定义了文件系统中的文件控制块模型


#### Controller

下文叙述时“磁盘”指本程序中的“虚拟磁盘”；当提到“真实物理磁盘”时，才是指用户电脑的“物理磁盘”

1. DiskManager.java

	该部分负责磁盘位图的管理，以及请求/释放空间

2. IO.java

	该部分负责对磁盘的读写

3. MainController.java

	该部分是项目中最主要的部分，负责Model与View的交流，通过Model的数据来填充View并展示给用户

4. SystemCore.java

	该部分是文件系统的核心，负责以下功能：
	
	- 创建文件
	- 创建文件夹
	- 删除文件
	- 删除文件夹
	- 读文件
	- 写文件
	- 进入目录
	- 离开目录
	- 格式化磁盘

## 实现思想

- 文件存储空间管理：连续结构
- 空间分配的方法：连续分配
- 空闲空间的管理：位图法
- 目录结构：树型目录结构

其中位图存放在前两个物理块中

## 改进之处

文件存储采用 ___连续结构___ 虽然容易实现，但是也有诸多缺点，文件不能动态增长，预留的空间如同一种浪费，采用 ___索引结构___ 会更好

另外，空间分配的方法其实与文件存储空间的管理方法有关，如果采用 ___索引结构___ ，那么空间可以按需分配，不需要给出预留空间

## 代码文档

___(见代码中的注释)___
