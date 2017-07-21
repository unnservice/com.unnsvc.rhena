
package com.unnsvc.rhena.common.model;

import java.io.Serializable;
import java.util.List;

public interface ILifecycleSpecification extends Iterable<IRhenaEdge>, Serializable {

	public boolean isResolved();

	public void setName(String name);

	public String getName();

	public List<IRhenaEdge> getLifecycleDependencies();

	public List<IProcessorSpec> processorIterator();

	public IContextSpec getContextReference();

	public List<IProcessorSpec> getProcessorReferences();

	public IGeneratorSpec getGeneratorReference();

	public List<ICommandSpec> getCommandReferences();

}
