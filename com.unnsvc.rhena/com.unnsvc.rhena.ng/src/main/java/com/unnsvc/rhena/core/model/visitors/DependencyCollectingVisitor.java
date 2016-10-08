
package com.unnsvc.rhena.core.model.visitors;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DependencyCollectingVisitor implements IVisitor {

	// private CompositeScope scope;
	private List<RhenaLifecycleExecution> dependencies;

	public DependencyCollectingVisitor(CompositeScope scope) {
		this(scope, new ArrayList<RhenaLifecycleExecution>());
	}

	public DependencyCollectingVisitor(CompositeScope scope, List<RhenaLifecycleExecution> dependencies) {

		// this.scope = scope;
		this.dependencies = dependencies;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

	}

	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		// TODO Auto-generated method stub

	}

	public List<RhenaLifecycleExecution> getDependencies() {

		return dependencies;
	}

}
