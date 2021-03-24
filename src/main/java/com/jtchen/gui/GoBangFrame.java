package com.jtchen.gui;

import com.jtchen.gui.panel.SettingPanel;
import com.jtchen.gui.panel.gobang.GoBangPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/23 19:19
 */
public class GoBangFrame extends JFrame {
	public static final int DEFAULT_WEIGTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	public static final int DEFAULT_X_LOCATION = 250;
	public static final int DEFAULT_Y_LOCATION = 100;

	public GoBangFrame() throws HeadlessException {
		GoBangPanel goBangPanel = new GoBangPanel();
		SettingPanel settingPanel = new SettingPanel(goBangPanel);

		// 默认设置
		defaultSetting();

		// 添加棋盘
		add(goBangPanel, BorderLayout.WEST);
		// 添加设置面板
		add(settingPanel, BorderLayout.EAST);


		// 设置是否可见
		setVisible(true);

	}


	// 默认设置
	private void defaultSetting() {

		// 设置图标
		ImageIcon imageIcon = new ImageIcon("src/main/resources/img/pic.jpg");
		setIconImage(imageIcon.getImage());

		// 设置标题
		setTitle("--GoBang--                - by jtchen");
		// 设置方位
		setLocation(DEFAULT_X_LOCATION, DEFAULT_Y_LOCATION);
		// 设置不可变大小
		setResizable(false);
		// 设置默认长宽
		setSize(DEFAULT_WEIGTH, DEFAULT_HEIGHT);

		// 设置关闭动作
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
