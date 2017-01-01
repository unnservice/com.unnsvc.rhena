
package com.unnsvc.rhena.common.model.lifecycle;

import java.util.List;

public interface ILifecycleReference {

	public String getName();

	public List<IProcessorReference> getProcessors();

	public IGeneratorReference getGenerator();

	public IExecutionReference getContext();

	/**
	 * Convenienc method to get all references
	 * 
	 * @return
	 */
	public List<ILifecycleProcessorReference> getAllReferences();

}
