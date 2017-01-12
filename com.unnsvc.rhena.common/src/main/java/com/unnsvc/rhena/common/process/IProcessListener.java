
package com.unnsvc.rhena.common.process;

import java.util.EventListener;

public interface IProcessListener extends EventListener {

	public void onProcess(Process process);
}