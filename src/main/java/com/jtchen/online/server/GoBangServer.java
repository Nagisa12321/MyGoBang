package com.jtchen.online.server;

import com.alibaba.fastjson.JSONObject;
import com.jtchen.beans.Chess;
import com.jtchen.beans.GoBangGame;
import com.jtchen.beans.GoBangPlayer;
import com.jtchen.command.Command;
import com.jtchen.command.impl.server.ServerSendMessageCommand;
import com.jtchen.game.Game;
import com.jtchen.online.GoBangExecutor;
import com.jtchen.utils.MessageUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 12:15
 */
public class GoBangServer implements Runnable {

	private static final Logger logger = Logger.getLogger(GoBangServer.class);

	private final int port;
	private Game game;
	private final BlockingQueue<Command> queue;
	private final Vector<Socket> sockets;
	private final Vector<Integer> colors;

	public GoBangServer(int port) {
		sockets = new Vector<>();
		this.port = port;
		this.game = new GoBangGame();
		this.colors = new Vector<>();
		colors.add(Chess.WHITE);
		queue = new LinkedBlockingQueue<>();

		// ����Command�����߳�
		Runnable goBangExecutor = new GoBangExecutor(queue);
		Thread t = new Thread(goBangExecutor);
		t.start();
	}


	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("�������ѿ���, �˿ں�" + port);
			while (true) {
				Socket socket = serverSocket.accept();

				InputStream is = socket.getInputStream();

				/*
				��Σ��ڽ��նˣ���Ҫָ�� read() ���� -1 ����Ϊ���ͽ�����
				�ſ�ʼ�����յ������ݡ���Ϊ�������ݡ��Ĵ���������Լ�����
				���ݱ߽磬Ҳ����˵����Ľ��ճ�����ʱ��Ӧ����ȷ֪������ǰ
				������Ƭ���Ƿ�����ϡ������Ƿ�Ҫ����������������ݡ���
				 */
				int data;
				StringBuilder builder = new StringBuilder();

				while ((data = is.read()) != -1) {
					builder.append((char) data);
					if (data == '}') break;
				}

				// ��ȡת��ΪJSON����
				JSONObject json = (JSONObject) JSONObject.parse(builder.toString());


				logger.info("�������յ���Ϣ" + json);
				int type = json.getInteger("type");
				//	{"type":0,"name":...,"color":...}
				if (type == 0) {
					if (game.isStart()) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "��ǰ�Ծ��Ѿ���ʼ��, ���Ժ��Խ���");
						queue.put(command);
						continue;
					}

					String name = json.getString("name");
					int color = json.getInteger("color");
					if (color == Chess.WHITE && game.getWhitePlayer() != null) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "��ɫ�����Ѿ�����ҳ�����, �����������");
						queue.put(command);
						continue;
					}
					if (color == Chess.BLACK && game.getBlackPlayer() != null) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "��ɫ�����Ѿ�����ҳ�����, �����������");
						queue.put(command);
						continue;
					}

					GoBangPlayer player = new GoBangPlayer(socket, name, color);
					if (color == Chess.BLACK) game.setBlackPlayer(player);
					else game.setWhitePlayer(player);

					// ������׽��ּ�����
					sockets.add(socket);
					// �½�һ�������߳�
					Runnable recipient = new GoBangServerRecipient(colors, sockets, queue, player, game);
					new Thread(recipient).start();

					if (game.getWhitePlayer() != null && game.getBlackPlayer() != null) {
						game.start();
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(sockets, "��Ϸ��ʼ!");
						queue.put(command);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			MessageUtils.openDialog(port + " �˿��Ѿ���ռ����, ��������˿�����");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
