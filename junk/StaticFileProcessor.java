package com.rudolfschmidt.alkun.processes;

import com.rudolfschmidt.alkun.handlers.ProcessHandler;

public class StaticFileProcessor implements Processor {

	private final String staticFileFolder;

	public StaticFileProcessor(String staticFileFolder) {
		this.staticFileFolder = staticFileFolder;
	}

	@Override
	public boolean execute(ProcessHandler processHandler) throws Exception {
		return processHandler.handle(staticFileFolder);
	}
}
