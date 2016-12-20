
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.EntryPoint;

/**
 * @TODO implement an internal context for saving thirngs during execution,
 *       which can be accessed through the RhenaCOntext
 * @author noname
 *
 */
public class RhenaContext implements IRhenaContext {

	private IRhenaCache cache;
	private IRhenaConfiguration config;
	private CascadingModelResolver cascadingResolver;
	private CascadingModelBuilder cascadingBuilder;

	public RhenaContext(IRhenaConfiguration config) {

		cache = new RhenaCache();
		this.config = config;
		this.cascadingResolver = new CascadingModelResolver(config, cache);
		this.cascadingBuilder = new CascadingModelBuilder(config, cache);
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		/**
		 * We resolve its prototype to ensure we get the maximum coverage in
		 * cyclic check
		 */
		IEntryPoint entryPoint = new EntryPoint(EExecutionType.PROTOTYPE, identifier);
		return cascadingResolver.resolveEdge(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, module.getIdentifier());
		return cascadingBuilder.buildEdge(entryPoint);
	}

	@Override
	public IRhenaCache getCache() {

		return cache;
	}

	@Override
	public IRhenaConfiguration getConfiguration() {

		return config;
	}
}
