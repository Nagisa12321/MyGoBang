package com.jtchen.online.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtchen.command.Command;
import com.jtchen.command.impl.client.ClientPutChessCommand;
import com.jtchen.command.impl.client.RemoveAllChessCommand;
import com.jtchen.command.impl.client.RemoveChessCommand;
import com.jtchen.command.impl.client.ShowMessageCommand;
import com.jtchen.gui.panel.gobang.GoBangPanel;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:25
 */
public class GoBangClientRecipient implements Runnable {
	/*
	put chess command: 		{"type":0,"address":[x,y],"color":0/1}
	remove chess command: 	{"type":1,"address":[x,y]}
	message command: 		{"type":2,"message":"..."}
	 */

	// 负责接收消息
	// 转化为Command对象
	// 放入操作队列之中
	// 等待GoBang执行者线程执行
	private final BlockingQueue<Command> queue;
	private final InputStream is;
	private final GoBangPanel panel;

	public GoBangClientRecipient(BlockingQueue<Command> queue, InputStream is, GoBangPanel panel) {
		this.queue = queue;
		this.is = is;
		this.panel = panel;
	}

	@Override
	public void run() {
		while (true) {
			try {
				JSONObject json;
				int data;
				StringBuilder builder = new StringBuilder();

				while ((data = is.read()) != -1) {
					builder.append((char) data);
					if (data == '}') break;
				}

				// 读取转化为JSON对象
				json = (JSONObject) JSONObject.parse(builder.toString());

				int type = json.getInteger("type");
				switch (type) {
					case 0:
						JSONArray array = json.getJSONArray("address");
						char x = array.getString(0).charAt(0);
						int y = array.getInteger(1);
						int color = json.getInteger("color");
						Command command = new ClientPutChessCommand(panel, x, y, color);
						queue.put(command);
						break;
					case 1:
						array = json.getJSONArray("address");
						x = array.getString(0).charAt(0);
						y = array.getInteger(1);
						color = json.getInteger("color");
						command = new RemoveChessCommand(panel, x, y, color);
						queue.put(command);
						break;
					case 2:
						String message = json.getString("message");
						command = new ShowMessageCommand(message);
						queue.put(command);
						break;
					case 4:
						message = json.getString("message");
						command = new RemoveAllChessCommand(panel, message);
						queue.put(command);
						break;
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
