package com.jtchen.command.impl;

import com.jtchen.command.Command;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 14:59
 */
public abstract class NoUndoCommand implements Command {

	// �޷�����֮����
	@Override
	final public void undo() {

	}
}
