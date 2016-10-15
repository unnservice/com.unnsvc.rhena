
package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;

import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public interface IExecutionContext extends ILifecycleProcessor {

	public List<IResource> getResources(IExecutionType execution);

}
