package com.jtchen.command.impl.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jtchen.beans.Chess;
import com.jtchen.command.Command;
import com.jtchen.game.Game;
import com.jtchen.game.Player;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 14:57
 */
public class ServerPutChessCommand implements Command {
	private static final Logger logger = Logger.getLogger(ServerPutChessCommand.class);

	// ��������������, �ж��Ƿ�ɹ�
	// ���ɹ�, Ҫ��ÿ���ͻ���������ɹ���Ϣ
	public String getCommandName() {
		return "ServerPutChessCommand";
	}

	private Game game;
	private final char x;
	private final int y;
	private final Player player;
	private final Vector<Socket> sockets;
	Vector<Integer> colors;

	public ServerPutChessCommand(Vector<Integer> colors, Vector<Socket> sockets, Player player, Game game, char x, int y) {
		logger.info("��ʼ��: " + game);
		this.player = player;
		this.game = game;
		this.sockets = sockets;
		this.x = x;
		this.y = y;
		this.colors = colors;
	}


	// put chess command: 		{"type":0,"address":[x,y],"color":0/1}
	@Override
	public void excute() {
		logger.info("excute: " + game);
		int nowColor = colors.get(0);
		// ��ǰ���Ǹ���ҵĻغ�
		if (nowColor != player.getColor()) return;

		boolean putChess = game.putChess(x, y, nowColor);

		// ����ɹ�, ��ʾû���ظ�������
		if (putChess) {
			colors.remove(0);
			if (nowColor == Chess.WHITE) colors.add(Chess.BLACK);
			else colors.add(Chess.WHITE);

			JSONObject json = new JSONObject();
			json.put("type", 0);
			JSONArray array = new JSONArray(Arrays.asList(x, y));
			json.put("address", array);
			json.put("color", nowColor);

			// ���͸�ÿ�����, ������µ�ͼ
			try {
				for (Socket socket : sockets) {
					OutputStream os = socket.getOutputStream();

					OutputStreamWriter writer = new OutputStreamWriter(os);
					writer.write(json.toJSONString());
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (game.isOver()) {
			Player winner = game.getWinner();
			JSONObject json = new JSONObject();
			json.put("type", 4);
			json.put("message", winner.getName() + " �Ѿ����ʤ��, ��Ϸ����");

			try {
				for (Socket socket : sockets) {
					OutputStream os = socket.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(os);
					writer.write(json.toJSONString());
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			game.shutDown();
		}

		logger.info("����excuteǰ: " + game);
	}

	@Override
	public void undo() {
		game.removeChess(x, y, colors.get(0));
	}
}
