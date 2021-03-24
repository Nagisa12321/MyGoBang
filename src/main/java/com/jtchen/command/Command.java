package com.jtchen.command;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:29
 */
public interface Command {
	void excute();

	void undo();

	String getCommandName();
}
