package com.jtchen.game;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 1:09
 */
public interface Game {

	boolean start();

	boolean isStart();

	boolean isOver();

	Player getWinner();

	void shutDown();

	void setBlackPlayer(Player player);

	Player getBlackPlayer();

	void setWhitePlayer(Player player);

	Player getWhitePlayer();

	boolean putChess(char x, int y, int color);

	boolean removeChess(char x, int y, int color);
}
