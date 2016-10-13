
package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;

public interface ILifecycleDeclaration {

	public String getName();

	public List<IProcessorReference> getProcessors();

	public IGeneratorReference getGenerator();

	public IExecutionReference getContext();

}
