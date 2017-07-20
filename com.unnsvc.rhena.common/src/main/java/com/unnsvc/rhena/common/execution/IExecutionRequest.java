
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.objectserver.messages.IRequest;

public interface IExecutionRequest extends IRequest {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

	public ILifecycleInstance getLifecycle();

	public IDependencies getDependencies();

}
