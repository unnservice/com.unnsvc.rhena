
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.objectserver.old.IObjectClient;

public interface IRhenaAgentClient extends IObjectClient<IExecutionRequest, IExecutionResult> {

}
