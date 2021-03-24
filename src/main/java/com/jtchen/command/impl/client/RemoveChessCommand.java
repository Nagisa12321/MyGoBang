package com.jtchen.command.impl.client;

import com.jtchen.beans.Chess;
import com.jtchen.command.Command;
import com.jtchen.command.impl.NoUndoCommand;
import com.jtchen.gui.panel.gobang.GoBangPanel;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:38
 */
public class RemoveChessCommand extends NoUndoCommand {

	public String getCommandName() {
		return "RemoveChessCommand";
	}
	// 指定JPanel完成悔棋动作
	private final GoBangPanel panel;
	private final Chess chess;

	public RemoveChessCommand(GoBangPanel panel, char x, int y, int color) {
		this.panel = panel;
		this.chess = new Chess(x, y, color);
	}

	@Override
	public void excute() {
		panel.removeChess(chess);
	}
}
