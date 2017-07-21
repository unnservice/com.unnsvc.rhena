
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.resolution.CascadingModelBuilder;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.core.status.StatusCascadingModelBuilder;
import com.unnsvc.rhena.repository.RhenaResolver;

public class RhenaEngine implements IRhenaEngine {

	private IRhenaContext context;

	public RhenaEngine(IRhenaConfiguration config) {

		this.context = new RhenaContext(config, new RhenaCache(), new RhenaResolver(), new RhenaFactories());
	}

	public RhenaEngine(IRhenaContext context) {

		this.context = context;
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		CascadingModelResolver modelResolver = new CascadingModelResolver(getContext());
		return modelResolver.resolveModuleTree(identifier);
	}

	@Override
	public IExecutionResponse resolveExecution(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		CascadingModelBuilder modelBuilder = new StatusCascadingModelBuilder(getContext());
		return modelBuilder.executeModel(type, identifier);
	}

	/**
	 * Convenience method
	 * 
	 * @throws RhenaException
	 */
	@Override
	public IExecutionResponse resolveExecution(EExecutionType type, IRhenaModule module) throws RhenaException {

		return this.resolveExecution(type, module.getIdentifier());
	}

	@Override
	public IRhenaContext getContext() {

		return context;
	}
}
