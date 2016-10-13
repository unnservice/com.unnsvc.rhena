
package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;

import com.unnsvc.rhena.common.execution.ExecutionType;

public interface IExecutionContext extends ILifecycleProcessor {

	public List<IResource> getResources(ExecutionType execution);

}
