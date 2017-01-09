
package com.unnsvc.rhena.core;

import java.util.Set;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.EntryPoint;
import com.unnsvc.rhena.core.visitors.RootFinder;

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

	public RhenaEngine(IRhenaContext context) {

		this.context = context;
		this.cascadingResolver = new CascadingModelResolver(context, context.getCache());
		this.cascadingBuilder = new CascadingModelBuilder(context, context.getCache());
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		/**
		 * We resolve its test to ensure we get the maximum coverage in cyclic
		 * check
		 */
		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);
		return cascadingResolver.resolveEntryPoint(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, module.getIdentifier());
		return cascadingBuilder.buildEntryPoint(entryPoint);
	}

	@Override
	public IRhenaContext getContext() {

		return context;
	}

	/**
	 * Finds the roots from the model in the context, but only roots which can
	 * reach the module identifier
	 * 
	 * @param identifier
	 * @param type
	 * @return
	 */
	public Set<ModuleIdentifier> findRoots(ModuleIdentifier identifier, EExecutionType type) {

		RootFinder finder = new RootFinder(context, identifier, type, identifier.getComponentName().toString(), identifier.getComponentName().toString());
		return finder.findRoots(0);
	}
}
