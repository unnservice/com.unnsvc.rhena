package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IModuleExecutor {

	public void submit(IRhenaBuilder builder);

	public void addCallback(IModuleExecutorCallback callback);

	public void close() throws RhenaException;

}
