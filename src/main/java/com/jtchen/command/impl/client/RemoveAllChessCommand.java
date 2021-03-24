package com.jtchen.command.impl.client;

import com.jtchen.command.impl.NoUndoCommand;
import com.jtchen.gui.panel.gobang.GoBangPanel;
import com.jtchen.utils.MessageUtils;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 23:42
 */
public class RemoveAllChessCommand extends NoUndoCommand {

	private final GoBangPanel panel;
	private final String message;

	public RemoveAllChessCommand(GoBangPanel panel, String message) {
		this.panel = panel;
		this.message = message;
	}

	@Override
	public void excute() {
		MessageUtils.openDialog(message);
		panel.removeAllChess();
	}

	@Override
	public String getCommandName() {
		return "RemoveAllChessCommand";
	}
}
