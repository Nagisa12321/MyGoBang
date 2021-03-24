package com.jtchen.beans;

import com.jtchen.game.Map;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 12:32
 */
public class GoBangMap implements Map {

	private final ChessType[][] map;

	private int winColor;

	public GoBangMap() {
		map = new ChessType[15][15];
		// 初始化棋盘
		clear();
	}

	@Override
	public boolean putChess(char x, int y, int color) {
		if (map[x - 'a'][15 - y] != ChessType.NULL)
			return false;

		else map[x - 'a'][15 - y] =
				color == Chess.WHITE ? ChessType.WHITE : ChessType.BLACK;
		return true;
	}

	@Override
	public boolean isOver() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				ChessType type = map[i][j];
				if (type == ChessType.NULL) continue;

				// 判断八个方向是否有五子连
				if (isLeftWin(i, j, type) ||
						isLeftUpWin(i, j, type) ||
						isUpWin(i, j, type) ||
						isRightUpWin(i, j, type) ||
						isRightWin(i, j, type) ||
						isRightDownWin(i, j, type) ||
						isDownWin(i, j, type) ||
						isLeftDownWin(i, j, type)) {
					winColor = type == ChessType.WHITE ? Chess.WHITE : Chess.BLACK;
					return true;
				}
			}
		}

		return false;
	}

	private boolean isLeftWin(int x, int y, ChessType type) {
		if (y < 5) return false;

		return map[x][y - 1] == type &&
				map[x][y - 2] == type &&
				map[x][y - 3] == type &&
				map[x][y - 4] == type;
	}

	private boolean isLeftUpWin(int x, int y, ChessType type) {
		if (x < 5 || y < 5) return false;

		return map[x - 1][y - 1] == type &&
				map[x - 2][y - 2] == type &&
				map[x - 3][y - 3] == type &&
				map[x - 4][y - 4] == type;
	}

	private boolean isUpWin(int x, int y, ChessType type) {
		if (x < 5) return false;

		return map[x - 1][y] == type &&
				map[x - 2][y] == type &&
				map[x - 3][y] == type &&
				map[x - 4][y] == type;
	}

	private boolean isRightUpWin(int x, int y, ChessType type) {
		if (x < 5 || y > 10) return false;

		return map[x - 1][y + 1] == type &&
				map[x - 2][y + 2] == type &&
				map[x - 3][y + 3] == type &&
				map[x - 4][y + 4] == type;
	}

	private boolean isRightWin(int x, int y, ChessType type) {
		if (y > 10) return false;

		return map[x][y + 1] == type &&
				map[x][y + 2] == type &&
				map[x][y + 3] == type &&
				map[x][y + 4] == type;
	}

	private boolean isRightDownWin(int x, int y, ChessType type) {
		if (x > 10 || y > 10) return false;

		return map[x + 1][y + 1] == type &&
				map[x + 2][y + 2] == type &&
				map[x + 3][y + 3] == type &&
				map[x + 4][y + 4] == type;
	}

	private boolean isDownWin(int x, int y, ChessType type) {
		if (x > 10) return false;

		return map[x + 1][y] == type &&
				map[x + 2][y] == type &&
				map[x + 3][y] == type &&
				map[x + 4][y] == type;
	}

	private boolean isLeftDownWin(int x, int y, ChessType type) {
		if (x > 10 || y < 5) return false;

		return map[x + 1][y - 1] == type &&
				map[x + 2][y - 2] == type &&
				map[x + 3][y - 3] == type &&
				map[x + 4][y - 4] == type;
	}


	@Override
	public int getWinColor() {
		return winColor;
	}

	@Override
	public void clear() {
		winColor = 2;
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				map[i][j] = ChessType.NULL;
			}
		}
	}

	@Override
	public boolean removeChess(char x, int y) {
		if (map[x - 'a'][15 - y] == ChessType.NULL) return false;

		else map[x - 'a'][15 - y] = ChessType.NULL;
		return true;
	}

	enum ChessType {BLACK, WHITE, NULL}
}
