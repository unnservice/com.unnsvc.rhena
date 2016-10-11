
package com.unnsvc.rhena.core.visitors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.RhenaExecution;

public class DependencyCollectionVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private ExecutionType requested;
	private List<ModuleIdentifier> identifiers;

	public DependencyCollectionVisitor(IResolutionContext context, ExecutionType requested) {

		this.context = context;
		this.requested = requested;
		this.identifiers = new ArrayList<ModuleIdentifier>();
	}

	@Override
	public void startModel(IRhenaModule model) throws RhenaException {

		for (IRhenaEdge edge : model.getDependencyEdges()) {

			if (edge.getExecutionType() == ExecutionType.DELIVERABLE) {
				edge.getTarget().visit(this);
				if (!identifiers.contains(edge.getTarget())) {
					identifiers.add(edge.getTarget().getModuleIdentifier());
				}
			}
		}
	}

	@Override
	public void endModel(IRhenaModule model) throws RhenaException {

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
