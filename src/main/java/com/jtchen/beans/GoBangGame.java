package com.jtchen.beans;

import com.jtchen.game.Game;
import com.jtchen.game.Map;
import com.jtchen.game.Player;

import org.apache.log4j.Logger;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 12:17
 */
public class GoBangGame implements Game {
	private static final Logger logger = Logger.getLogger(GoBangGame.class);
	private final Map goBangMap;
	private boolean start;
	private Player blackPlayer;
	private Player whitePlayer;

	public GoBangGame() {
		this.start = false;
		this.goBangMap = new GoBangMap();
		blackPlayer = null;
		whitePlayer = null;
	}

	public Player getBlackPlayer() {
		return blackPlayer;
	}

	public Player getWhitePlayer() {
		return whitePlayer;
	}

	@Override
	public synchronized boolean start() {
		if (blackPlayer == null || whitePlayer == null) return false;

		start = true;
		return true;
	}

	@Override
	public synchronized boolean isStart() {
		return this.start;
	}

	@Override
	public synchronized boolean isOver() {
		return goBangMap.isOver();
	}

	@Override
	public synchronized Player getWinner() {
		if (!isOver()) return null;

		int winColor = goBangMap.getWinColor();
		if (winColor == Chess.BLACK) return blackPlayer;
		else return whitePlayer;
	}

	@Override
	public synchronized void shutDown() {
		start = false;
		goBangMap.clear();
	}

	@Override
	public synchronized void setBlackPlayer(Player player) {
		this.blackPlayer = player;
	}

	@Override
	public synchronized void setWhitePlayer(Player player) {
		this.whitePlayer = player;
	}


	@Override
	public boolean putChess(char x, int y, int color) {
		logger.info("当前所下的棋子: (" + x + ',' + y + ')');

		return goBangMap.putChess(x, y, color);
	}

	@Override
	public synchronized boolean removeChess(char x, int y, int color) {

		return goBangMap.putChess(x, y, color);
	}



}
