
package com.unnsvc.rhena.core.lifecycle;

import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorExecutable;

public class ProcessorExecutable implements ILifecycleProcessorExecutable {

	private static final long serialVersionUID = 1L;
	private String clazz;

	public ProcessorExecutable(String clazz) {

		this.clazz = clazz;
	}
	
	@Override
	public String getClazz() {

		return clazz;
	}
}
