
package com.unnsvc.rhena.core;

import java.util.Set;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IModelBuilder;
import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.search.RootFinder;
import com.unnsvc.rhena.core.model.EntryPoint;

/**
 * @TODO implement an internal context for saving thirngs during execution,
 *       which can be accessed through the RhenaCOntext
 * @author noname
 *
 */
public class RhenaEngine implements IRhenaEngine {

	private IRhenaContext context;
	private IModelResolver modelResolver;
	private IModelBuilder modelBuilder;

	public RhenaEngine(IRhenaContext context) {

		this.context = context;
		this.modelResolver = new CascadingModelResolver(context, context.getCache());
		this.modelBuilder = new CascadingModelBuilder(context, context.getCache());
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		// Get maximum coverage in model resolution with test
		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);
		return modelResolver.resolveEntryPoint(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(ICaller caller) throws RhenaException {

		return modelBuilder.buildEntryPoint(caller);
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
	@Override
	public Set<ModuleIdentifier> findRoots(ModuleIdentifier identifier, EExecutionType type) {

		RootFinder finder = new RootFinder(context.getCache(), identifier, type, identifier.getComponentName().toString(), identifier.getComponentName().toString());
		return finder.findRoots(0);
	}

}
