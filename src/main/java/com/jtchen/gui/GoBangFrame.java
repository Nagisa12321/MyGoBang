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

		// Ĭ������
		defaultSetting();

		// �������
		add(goBangPanel, BorderLayout.WEST);
		// ����������
		add(settingPanel, BorderLayout.EAST);


		// �����Ƿ�ɼ�
		setVisible(true);

	}


	// Ĭ������
	private void defaultSetting() {

		// ����ͼ��
		ImageIcon imageIcon = new ImageIcon("src/main/resources/img/pic.jpg");
		setIconImage(imageIcon.getImage());

		// ���ñ���
		setTitle("--GoBang--                - by jtchen");
		// ���÷�λ
		setLocation(DEFAULT_X_LOCATION, DEFAULT_Y_LOCATION);
		// ���ò��ɱ��С
		setResizable(false);
		// ����Ĭ�ϳ���
		setSize(DEFAULT_WEIGTH, DEFAULT_HEIGHT);

		// ���ùرն���
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
