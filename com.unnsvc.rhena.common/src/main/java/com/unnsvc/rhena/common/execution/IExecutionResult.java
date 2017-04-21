
package com.unnsvc.rhena.common.execution;

import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.objectserver.IObjectReply;

public interface IExecutionResult extends IObjectReply {

	public IEntryPoint getEntryPoint();

	public IRhenaModule getModule();

}
