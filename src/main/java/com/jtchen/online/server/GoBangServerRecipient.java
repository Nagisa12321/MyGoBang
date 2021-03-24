package com.jtchen.online.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtchen.beans.GoBangGame;
import com.jtchen.command.Command;
import com.jtchen.command.impl.server.ServerPutChessCommand;
import com.jtchen.command.impl.server.ServerSendMessageCommand;
import com.jtchen.game.Game;
import com.jtchen.game.Player;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 15:30
 */
public class GoBangServerRecipient implements Runnable {
	private static final Logger logger = Logger.getLogger(GoBangServerRecipient.class);
	// TCP���������ȶ�������
	// һ��reciver�̶߳�Ӧһ�����
	// ���ҹ���һ��game����

	private final Player player;
	private Game game;
	private final BlockingQueue<Command> queue;
	private final Vector<Socket> sockets;Vector<Integer> colors;

	public GoBangServerRecipient(Vector<Integer> colors, Vector<Socket> sockets, BlockingQueue<Command> queue, Player player, Game game) {
		this.queue = queue;
		this.sockets = sockets;
		this.player = player;
		this.game = game;
		this.colors = colors;
	}

	@Override
	public void run() {
		try {
			InputStream is = player.getSocket().getInputStream();
			while (true) {
				JSONObject json;
				int data;
				StringBuilder builder = new StringBuilder();

				while ((data = is.read()) != -1) {
					builder.append((char) data);
					if (data == '}') break;
				}

				// ��ȡת��ΪJSON����
				json = (JSONObject) JSONObject.parse(builder.toString());


				// TODO: 2021/3/24 ��ѯ����
				if (json == null) continue;

				int type = json.getInteger("type");

				if (type == 1) {
					if (!game.start()) {
						ServerSendMessageCommand command = new ServerSendMessageCommand(player.getSocket(), "��Ϸ��û��ʼ, ��ȴ�������ҽ�����Ϸ");
						queue.put(command);
						continue;
					}

					JSONArray address = json.getJSONArray("address");
					char x = address.getString(0).charAt(0);
					int y = address.getInteger(1);

					logger.info("����ǰ: " + game );
					ServerPutChessCommand command = new ServerPutChessCommand(colors, sockets, player, game, x, y);
					queue.put(command);
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}
}

