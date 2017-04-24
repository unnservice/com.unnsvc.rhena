
package com.unnsvc.rhena.common.lifecycle;

import java.io.Serializable;
import java.util.List;

import com.unnsvc.rhena.common.traversal.IDependencies;

public interface ILifecycleInstance extends Serializable, Iterable<IProcessorInstance> {

	public String getLifecycleName();

	public IDependencies getDependencies();

	public IContextInstance getContext();

	public List<IProcessorInstance> getProcessors();

	public IGeneratorInstance getGenerator();

	public List<ICommandInstance> getCommands();

}
