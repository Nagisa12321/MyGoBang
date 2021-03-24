package com.jtchen.game;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 1:15
 */
public interface Map {

	boolean putChess(char x, int y, int color);

	boolean isOver();

	int getWinColor();

	void clear();

	boolean removeChess(char x, int y);
}
