
package com.unnsvc.rhena.common.lifecycle;

import java.util.Iterator;
import java.util.List;

import com.unnsvc.rhena.common.model.ICommandSpec;
import com.unnsvc.rhena.common.model.IContextSpec;
import com.unnsvc.rhena.common.model.IGeneratorSpec;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IProcessorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;

public class UnresolvedLifecycleSpecification implements ILifecycleSpecification {

	private static final long serialVersionUID = 1L;
	private String name;

	public UnresolvedLifecycleSpecification(String name) {

		this.name = name;
	}

	@Override
	public Iterator<IRhenaEdge> iterator() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public void setName(String name) {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public List<IRhenaEdge> getLifecycleDependencies() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public List<IProcessorSpec> processorIterator() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public IContextSpec getContextReference() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public List<IProcessorSpec> getProcessorReferences() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public IGeneratorSpec getGeneratorReference() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

	@Override
	public List<ICommandSpec> getCommandReferences() {

		throw new UnsupportedOperationException("Not implemented for unresolved reference");
	}

}
