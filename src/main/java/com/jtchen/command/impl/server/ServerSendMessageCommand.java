package com.jtchen.command.impl.server;

import com.alibaba.fastjson.JSONObject;
import com.jtchen.command.impl.NoUndoCommand;
import com.jtchen.online.GoBangExecutor;
import com.jtchen.utils.MessageUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 16:23
 */
public class ServerSendMessageCommand extends NoUndoCommand {
	private static final Logger logger  = Logger.getLogger(GoBangExecutor.class);
	public String getCommandName() {
		return "ServerSendMessageCommand";
	}
	private Socket socket = null;
	private Vector<Socket> sockets = null;
	private final String message;

	public ServerSendMessageCommand(Socket socket, String message) {
		this.socket = socket;
		this.message = message;
	}

	public ServerSendMessageCommand(Vector<Socket> sockets, String message) {
		this.sockets = sockets;
		this.message = message;
	}

	// message command: 		{"type":2,"message":"..."}
	@Override
	public void excute() {
		JSONObject json = new JSONObject();
		json.put("type", 2);
		json.put("message", message);
		try {
			if (sockets == null) {
				OutputStream os = socket.getOutputStream();
				OutputStreamWriter writer = new OutputStreamWriter(os);
				writer.write(json.toJSONString());
				writer.flush();
			} else {
				for (Socket socket : sockets) {
					logger.info("ServerSendMessageCommand 同时给两个socket发东西" );
					OutputStream os = socket.getOutputStream();
					OutputStreamWriter writer = new OutputStreamWriter(os);
					writer.write(json.toJSONString());
					writer.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
