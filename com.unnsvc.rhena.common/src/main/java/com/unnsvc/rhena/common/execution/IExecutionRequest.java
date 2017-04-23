
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.objectserver.IObjectRequest;

public interface IExecutionRequest extends IObjectRequest {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

	public IDependencies getDependencies();

}
