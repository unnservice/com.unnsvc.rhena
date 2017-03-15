package com.unnsvc.rhena.common.agent;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;

public interface IAgentClient {

	public ILifecycleExecutionResult executeLifecycle(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable);

}
