
package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;

public interface IExecutionContext extends ILifecycleProcessor {

	public List<IResource> getResources();

}
