package com.jtchen.command.impl.client;

import com.jtchen.command.impl.NoUndoCommand;
import com.jtchen.utils.MessageUtils;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:40
 */
public class ShowMessageCommand extends NoUndoCommand {
	private final String message;
	public String getCommandName() {
		return "ShowMessageCommand";
	}
	public ShowMessageCommand(String message) {
		// 完成消息提示操作
		this.message = message;
	}

	@Override
	public void excute() {
		MessageUtils.openDialog(message);
	}
}
