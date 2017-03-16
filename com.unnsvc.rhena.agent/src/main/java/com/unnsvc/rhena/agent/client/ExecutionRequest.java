
package com.unnsvc.rhena.agent.client;

import java.io.Serializable;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.agent.IExecutionRequest;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;

public class ExecutionRequest implements IExecutionRequest, Serializable {

	private static final long serialVersionUID = 1L;
	private IRhenaCache cache;
	private IRhenaConfiguration config;
	private ICaller caller;
	private ILifecycleExecutable lifecycleExecutable;

	public ExecutionRequest(IRhenaCache cache, IRhenaConfiguration config, ICaller caller, ILifecycleExecutable lifecycleExecutable) {

		this.cache = cache;
		this.config = config;
		this.caller = caller;
		this.lifecycleExecutable = lifecycleExecutable;
	}

	@Override
	public IRhenaCache getCache() {

		return cache;
	}

	@Override
	public IRhenaConfiguration getConfig() {

		return config;
	}

	@Override
	public ICaller getCaller() {

		return caller;
	}

	@Override
	public ILifecycleExecutable getLifecycleExecutable() {

		return lifecycleExecutable;
	}
}
