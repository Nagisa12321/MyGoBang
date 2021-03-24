package com.jtchen.beans;

import com.jtchen.game.Player;

import java.net.InetAddress;
import java.net.Socket;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 11:32
 */
public class GoBangPlayer implements Player {

	private Socket socket;
	private final String name;
	private final int color;

	public GoBangPlayer(Socket socket, String name, int color) {
		this.socket = socket;
		this.name = name;
		this.color = color;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public Socket getSocket() {
		return socket;
	}
}
