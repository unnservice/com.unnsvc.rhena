
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.RhenaReference;
import com.unnsvc.rhena.core.visitors.EdgeVisitor.EdgeHandler;

public class ModelInitialisingHandler implements EdgeHandler {

	private IResolutionContext context;

	public ModelInitialisingHandler(IResolutionContext context) {

		this.context = context;
	}

	@Override
	public void handleEdge(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		IRhenaModule target = context.materialiseModel(edge.getTarget().getModuleIdentifier());
		edge.setTarget(target);
	}

	@Override
	public boolean canEnter(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		if (edge.getTarget() instanceof RhenaReference) {

			return true;
		}
		return false;
	}

}
