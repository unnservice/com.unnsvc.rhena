
package com.unnsvc.rhena.common.process;

import com.unnsvc.rhena.common.IRhenaConfiguration;

public class ProcessExitTracker extends Thread {

	private Process process;
	private IRhenaConfiguration config;

	public ProcessExitTracker(Process process, IRhenaConfiguration config) {

		this.process = process;
		this.config = config;
	}

	public Process getProcess() {

		return process;
	}

	public void run() {

		try {
			process.waitFor();
			for (IProcessListener listener : config.getAgentExitListeners()) {
				listener.onProcess(process);
			}
		} catch (InterruptedException e) {
		}
	}
}