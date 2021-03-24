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

		// 开启Command处理线程
		Runnable goBangExecutor = new GoBangExecutor(queue);
		Thread t = new Thread(goBangExecutor);
		t.start();
	}


	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			logger.info("服务器已开启, 端口号" + port);
			while (true) {
				Socket socket = serverSocket.accept();

				InputStream is = socket.getInputStream();

				/*
				其次，在接收端，不要指望 read() 返回 -1 才认为发送结束，
				才开始处理收到的数据。作为“流数据”的处理，你必需自己定义
				数据边界，也就是说，你的接收程序随时都应该明确知道“当前
				的数据片断是否传输完毕”、“是否要继续接收下面的数据”。
				 */
				int data;
				StringBuilder builder = new StringBuilder();

				while ((data = is.read()) != -1) {
					builder.append((char) data);
					if (data == '}') break;
				}

				// 读取转化为JSON对象
				JSONObject json = (JSONObject) JSONObject.parse(builder.toString());


				logger.info("服务器收到信息" + json);
				int type = json.getInteger("type");
				//	{"type":0,"name":...,"color":...}
				if (type == 0) {
					if (game.isStart()) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "当前对局已经开始了, 请稍后尝试进入");
						queue.put(command);
						continue;
					}

					String name = json.getString("name");
					int color = json.getInteger("color");
					if (color == Chess.WHITE && game.getWhitePlayer() != null) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "白色棋子已经有玩家持有啦, 请更换其他的");
						queue.put(command);
						continue;
					}
					if (color == Chess.BLACK && game.getBlackPlayer() != null) {
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(socket, "黑色棋子已经有玩家持有啦, 请更换其他的");
						queue.put(command);
						continue;
					}

					GoBangPlayer player = new GoBangPlayer(socket, name, color);
					if (color == Chess.BLACK) game.setBlackPlayer(player);
					else game.setWhitePlayer(player);

					// 添加入套接字集合中
					sockets.add(socket);
					// 新建一个接收线程
					Runnable recipient = new GoBangServerRecipient(colors, sockets, queue, player, game);
					new Thread(recipient).start();

					if (game.getWhitePlayer() != null && game.getBlackPlayer() != null) {
						game.start();
						ServerSendMessageCommand command =
								new ServerSendMessageCommand(sockets, "游戏开始!");
						queue.put(command);
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
			MessageUtils.openDialog(port + " 端口已经被占用了, 请打开其他端口试试");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
