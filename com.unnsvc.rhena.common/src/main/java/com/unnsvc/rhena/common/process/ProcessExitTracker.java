
package com.unnsvc.rhena.common.process;

import java.util.List;

public class ProcessExitTracker extends Thread {

	private Process process;
	private List<IProcessListener> exitListeners;

	public ProcessExitTracker(Process process, List<IProcessListener> exitListeners) {

		this.process = process;
		this.exitListeners = exitListeners;
	}

	public void run() {

		try {
			process.waitFor();
			for (IProcessListener listener : exitListeners) {
				listener.onProcess(process);
			}
		} catch (InterruptedException e) {
		}
	}
}