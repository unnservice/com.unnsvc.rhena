
package com.unnsvc.rhena.common.agent;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.lifecycle.ILifecycleExecutable;

public interface IExecutionRequest {

	public IRhenaCache getCache();

	public IRhenaConfiguration getConfig();

	public ICaller getCaller();

	public ILifecycleExecutable getLifecycleExecutable();

}
