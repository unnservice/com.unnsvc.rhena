
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
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
public class RhenaEngine implements IRhenaEngine {

	private IRhenaContext context;
	private CascadingModelResolver cascadingResolver;
	private CascadingModelBuilder cascadingBuilder;
	private IRhenaCache cache;

	public RhenaEngine(IRhenaContext context) {

		this.context = context;
		this.cache = new RhenaCache(context);
		this.cascadingResolver = new CascadingModelResolver(context, cache);
		this.cascadingBuilder = new CascadingModelBuilder(context, cache);
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		/**
		 * We resolve its prototype to ensure we get the maximum coverage in
		 * cyclic check
		 */
		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);
		return cascadingResolver.resolveEdge(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, module.getIdentifier());
		return cascadingBuilder.buildEdge(entryPoint);
	}
	
	@Override
	public IRhenaContext getContext() {

		return context;
	}
	
	@Override
	public IRhenaCache getCache() {

		return cache;
	}
}
