package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.IRhenaBuilder;

public interface IModuleExecutor {

	public void submit(IRhenaBuilder builder);

	public void addCallback(IModuleExecutorCallback callback);

}
