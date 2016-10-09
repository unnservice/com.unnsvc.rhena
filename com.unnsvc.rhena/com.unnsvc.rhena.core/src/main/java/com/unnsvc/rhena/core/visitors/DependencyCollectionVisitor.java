
package com.unnsvc.rhena.core.visitors;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

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
	public void startModel(RhenaModel model) throws RhenaException {

		for (RhenaEdge edge : model.getDependencyEdges()) {

			// Not all scopes are transient, so we can't traverse all
			if (requested.compareTo(edge.getExecutionType()) <= 0) {
				context.materialiseModel(edge.getTarget()).visit(this);
				if (!identifiers.contains(edge.getTarget())) {
					identifiers.add(edge.getTarget());
				}
			}
		}
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

	}

	public List<ModuleIdentifier> getDependencyChain() {

		return identifiers;
	}
}
