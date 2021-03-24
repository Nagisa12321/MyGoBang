package com.jtchen.command.impl;

import com.jtchen.command.impl.client.ShowMessageCommand;
import org.junit.Test;

public class ShowMessageCommandTest {
	@Test
	public void test1() {
		ShowMessageCommand test = new ShowMessageCommand("just a test");
		test.excute();
	}
}