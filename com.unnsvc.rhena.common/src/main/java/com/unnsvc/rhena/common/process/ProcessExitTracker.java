
package com.unnsvc.rhena.common.process;

public class ProcessExitTracker extends Thread {

//	private Process process;
//	private IRhenaConfiguration config;
//	private IRhenaContext context;
//
//	public ProcessExitTracker(IRhenaContext context, Process process, IRhenaConfiguration config) {
//
//		this.process = process;
//		this.config = config;
//		this.context = context;
//	}
//
//	public Process getProcess() {
//
//		return process;
//	}
//
//	public void run() {
//
//		try {
//			process.waitFor();
//			for (IProcessListener listener : context.getAgentExitListeners()) {
//				listener.onProcess(process);
//			}
//		} catch (InterruptedException e) {
//		}
//	}
}