package com.jtchen.online.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtchen.beans.Address;
import com.jtchen.command.Command;
import com.jtchen.gui.panel.gobang.GoBangPanel;
import com.jtchen.online.GoBangExecutor;
import com.jtchen.online.server.GoBangServer;
import com.jtchen.utils.MessageUtils;
import org.apache.log4j.Logger;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:23
 */
public class GoBangClient {

	private static final Logger logger  = Logger.getLogger(GoBangClient.class);


	private final GoBangPanel panel;
	private final String hostname;
	private final int port;
	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private final int color;
	private final String name;
	private final BlockingQueue<Command> queue;

	public GoBangClient(GoBangPanel panel, String hostname, int port, int color, String name) {
		this.hostname = hostname;
		this.port = port;
		this.color = color;
		this.name = name;
		this.queue = new LinkedBlockingQueue<>();
		this.panel = panel;
		try {
			getSocketAndStream();
			sendBasicInformation();
		} catch (IOException e) {
			e.printStackTrace();
			MessageUtils.openDialog("Failed to connect to the server, please try again");
		}

		logger.info("已经向服务器发出 basic information, 准备开启收发线程");

		panel.addMouseListener(new DrawChessListener());
		// 开启收发线程
		GoBangClientRecipient recipient = new GoBangClientRecipient(queue, is, panel);
		new Thread(recipient).start();

		GoBangExecutor executor = new GoBangExecutor(queue);
		new Thread(executor).start();

	}

	/*
	{"type":0,"name":...,"color":...}
	 */
	private void sendBasicInformation() throws IOException {
		JSONObject json = new JSONObject();
		json.put("type", 0); // 基本信息
		json.put("name", name); // 玩家id
		json.put("color", color); // 棋子颜色

		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(json.toJSONString());
		writer.flush();

	}


	private void getSocketAndStream() throws IOException {
		socket = new Socket(hostname, port);
		os = socket.getOutputStream();
		is = socket.getInputStream();
	}

	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			MessageUtils.openDialog(e.getMessage());
		}
	}


	private class DrawChessListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {

			int x = e.getX();
			int y = e.getY();

			Address address = panel.parseMouseXY(x, y);

			// 向服务器发送下棋请求
			JSONObject json = new JSONObject();
			json.put("type", 1);
			json.put("address", new JSONArray(Arrays.asList(address.getX(), address.getY())));

			logger.info("x = " + address.getX() + ", y = " + address.getY() + ", " + json);
			try {
				OutputStreamWriter writer = new OutputStreamWriter(os);
				writer.write(json.toJSONString());
				writer.flush();
			} catch (IOException ioException) {
				ioException.printStackTrace();
				MessageUtils.openDialog("向服务器请求落子错误");
			}
		}
	}
}
