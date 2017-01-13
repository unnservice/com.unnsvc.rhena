
package com.unnsvc.rhena.common.lifecycle;

import java.io.Serializable;
import java.util.List;

public interface ILifecycleReference extends Serializable {

	public String getName();

	public List<ILifecycleProcessorReference> getProcessors();

	public ILifecycleProcessorReference getGenerator();

	public ILifecycleProcessorReference getContext();

	/**
	 * Convenienc method to get all references
	 * 
	 * @return
	 */
	public List<ILifecycleProcessorReference> getAllReferences();

	public List<ILifecycleProcessorReference> getCommands();

}
