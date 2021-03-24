package com.jtchen.online;

import com.jtchen.command.Command;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 10:27
 */
public class GoBangExecutor implements Runnable {
	private static final Logger logger  = Logger.getLogger(GoBangExecutor.class);

	private final BlockingQueue<Command> queue;

	public GoBangExecutor(BlockingQueue<Command> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true){
			try {
				Command command = queue.take();
				logger.info("GoBangExecutor正在处理命令" + command.getCommandName() );
				command.excute();
				logger.info(command.getCommandName() + "处理完成");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
