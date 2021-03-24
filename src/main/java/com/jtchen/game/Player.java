package com.jtchen.game;

import java.net.InetAddress;
import java.net.Socket;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 1:10
 */
public interface Player {

	// Íæ¼ÒĞÕÃû
	String getName();

	int getColor();

	Socket getSocket();
}
