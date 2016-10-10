
package com.unnsvc.rhena.core.visitors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DependencyCollectionVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private RhenaExecutionType requested;
	private List<ModuleIdentifier> identifiers;

	public DependencyCollectionVisitor(IResolutionContext context, RhenaExecutionType requested) {

		this.context = context;
		this.requested = requested;
		this.identifiers = new ArrayList<ModuleIdentifier>();
	}

	@Override
	public void startModel(RhenaModule model) throws RhenaException {

		for (RhenaEdge edge : model.getDependencyEdges()) {

			// Not all scopes are transient, so we can't traverse all
			if (requested.compareTo(edge.getExecutionType()) <= 0) {
				edge.getTarget().visit(this);
				if (!identifiers.contains(edge.getTarget())) {
					identifiers.add(edge.getTarget().getModuleIdentifier());
				}
			}
		}
	}

	@Override
	public void endModel(RhenaModule model) throws RhenaException {

	}

	public List<ModuleIdentifier> getDependencyChain() {

		return identifiers;
	}

	public List<URL> getDependencyChainURL() throws RhenaException {

		List<URL> execs = new ArrayList<URL>();
		for (ModuleIdentifier identifier : identifiers) {
			RhenaExecution exec = context.materialiseExecution(context.materialiseModel(identifier), requested);
			execs.add(exec.getArtifactURL());
		}
		return execs;
	}
}
