
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.core.resolution.CascadingModelBuilder2;
import com.unnsvc.rhena.core.resolution.CascadingModelResolver;
import com.unnsvc.rhena.repository.RhenaResolver;

public class RhenaEngine implements IRhenaEngine {

	private IRhenaCache cache;
	private IRhenaConfiguration config;
	private IRhenaResolver resolver;

	public RhenaEngine(IRhenaConfiguration config) {

		this.cache = new RhenaCache();
		this.config = config;
		this.resolver = new RhenaResolver(config);
	}

	@Override
	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		CascadingModelResolver modelResolver = new CascadingModelResolver(resolver, cache);
		IRhenaModule module = modelResolver.resolveModuleTree(identifier);
		return module;
	}

	@Override
	public IExecutionResult resolveExecution(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		CascadingModelBuilder2 modelBuilder = new CascadingModelBuilder2(config, cache, resolver);
		return modelBuilder.executeModel(type, identifier);
	}

	/**
	 * Convenience method
	 * 
	 * @throws RhenaException
	 */
	@Override
	public IExecutionResult resolveExecution(EExecutionType type, IRhenaModule module) throws RhenaException {

		return this.resolveExecution(type, module.getIdentifier());
	}
}
