
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class RhenaContext implements IRhenaContext {

	private CascadingModelResolver cascadingResolver;
	private CascadingModelBuilder cascadingBuilder;

	public RhenaContext(IRhenaConfiguration config) {

		this.cascadingResolver = new CascadingModelResolver(config);
		this.cascadingBuilder = new CascadingModelBuilder(config, cascadingResolver);
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		// We resolve its prototype to ensure we get the maximum coverage in cyclic check
		IRhenaEdge entryPoint = new RhenaEdge(EExecutionType.PROTOTYPE, identifier, ESelectionType.SCOPE);
		return cascadingResolver.resolveEdge(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IRhenaEdge entryPoint = new RhenaEdge(type, module.getIdentifier(), ESelectionType.SCOPE);
		return cascadingBuilder.buildEdge(entryPoint);
	}
}
