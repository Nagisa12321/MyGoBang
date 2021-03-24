package com.jtchen.gui.panel;

import com.jtchen.gui.GoBangFrame;
import com.jtchen.gui.panel.gobang.GoBangPanel;
import com.jtchen.online.client.GoBangClient;
import com.jtchen.online.server.GoBangServer;
import com.jtchen.utils.MessageUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/23 20:06
 */
public class SettingPanel extends JPanel {
	private static final Logger logger = Logger.getLogger(SettingPanel.class);
	private static final int PANEL_WEIGTH = GoBangFrame.DEFAULT_WEIGTH - GoBangFrame.DEFAULT_HEIGHT - 15;
	private static final int PANEL_HEIGHT = GoBangFrame.DEFAULT_HEIGHT;
	private final JButton startGame;
	private final JButton hostGame;
	private final JTextField ip;
	private final JTextField client_port;
	private final JTextField server_port;
	private final JTextField name;
	private JComboBox<String> box;
	private final GoBangPanel panel;

	public SettingPanel(GoBangPanel panel) {
		this.panel = panel;
		defaultSetting();

		// ¿ªÊ¼ÓÎÏ·
		JLabel port1 = new JLabel("PORT");
		JPanel start = new JPanel();
		start.setPreferredSize(new Dimension(PANEL_WEIGTH, PANEL_HEIGHT / 3));
		// host a game
		this.server_port = new JTextField();
		this.server_port.setPreferredSize(new Dimension(PANEL_WEIGTH - 80, 20));

		// ip/port
		JLabel ip = new JLabel("       IP");
		this.ip = new JTextField();
		this.ip.setPreferredSize(new Dimension(PANEL_WEIGTH - 70, 20));
		JLabel port2 = new JLabel("PORT");
		this.client_port = new JTextField();
		this.client_port.setPreferredSize(new Dimension(PANEL_WEIGTH - 70, 20));

		// name
		JLabel name = new JLabel("NAME");
		this.name = new JTextField();
		this.name.setPreferredSize(new Dimension(PANEL_WEIGTH - 120, 20));
		// color
		box = new JComboBox<>();
		box.addItem("ºÚ");
		box.addItem("°×");


		// botton
		startGame = new JButton("join the game");
		hostGame = new JButton("host a game");
		startGame.setPreferredSize(new Dimension(PANEL_WEIGTH - 10, 30));
		hostGame.setPreferredSize(new Dimension(PANEL_WEIGTH - 10, 30));


		start.add(port1);
		start.add(server_port);
		start.add(hostGame);

		start.add(ip);
		start.add(this.ip);
		start.add(port2);
		start.add(this.client_port);
		start.add(box);
		start.add(name);
		start.add(this.name);
		start.add(startGame);


		start.setBorder(BorderFactory.createTitledBorder("start game"));

		add(start, BorderLayout.SOUTH);

		// Ìí¼Ó¼àÌýÆ÷
		addListener();


		debugSetting();
	}

	private void debugSetting() {
		this.server_port.setText("5555");
		this.client_port.setText("5555");
		this.name.setText("jtchen");
		this.ip.setText("127.0.0.1");
	}

	private void addListener() {
		startGame.addActionListener(e -> {
			if (!checkPlayerInput()) return;
			String ip = this.ip.getText();
			String port = this.client_port.getText();
			String name = this.name.getText();
			int color = box.getSelectedIndex();
			new GoBangClient(panel, ip, Integer.parseInt(port), color, name);
		});

		hostGame.addActionListener(e -> {
			if (!checkHosterInput()) return;
			String port = this.server_port.getText();

			GoBangServer goBangServer = new GoBangServer(Integer.parseInt(port));
			Thread thread = new Thread(goBangServer);
			thread.start();
		});
	}

	private boolean checkPlayerInput() {
		if (this.ip.getText().equals("")) {
			MessageUtils.openDialog("ip is empty");
			return false;
		} else if (this.client_port.getText().equals("")) {
			MessageUtils.openDialog("client_port is empty");
			return false;
		} else if (this.name.getText().equals("")) {
			MessageUtils.openDialog("name is empty");
			return false;
		} else return true;
	}

	private boolean checkHosterInput() {
		String text = this.server_port.getText();
		if (text.equals("")) {
			MessageUtils.openDialog("server port is empty");
			return false;
		}

		try {
			int i = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			MessageUtils.openDialog("port should be number");
		}

		return true;
	}

	// Ä¬ÈÏÉèÖÃ
	private void defaultSetting() {
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(PANEL_WEIGTH, PANEL_HEIGHT));

		TitledBorder border = BorderFactory.createTitledBorder("setting");
		setBorder(border);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		// »­³ö±ß¿ò
		paintBorder(g2d);
	}

}
