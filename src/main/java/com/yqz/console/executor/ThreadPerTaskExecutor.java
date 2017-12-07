package com.yqz.console.executor;

import java.util.concurrent.Executor;

public class ThreadPerTaskExecutor implements Executor {
	public void execute(Runnable r) {
		new Thread(r).start();
	}
}
