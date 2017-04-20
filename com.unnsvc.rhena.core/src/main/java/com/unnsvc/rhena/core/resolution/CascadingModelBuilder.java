
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaBuilder;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.execution.IModuleExecutor;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.EModuleType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.execution.ModuleExecutor;
import com.unnsvc.rhena.execution.builders.RemoteBuilder;
import com.unnsvc.rhena.execution.builders.WorkspaceBuilder;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelBuilder extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IModuleExecutor moduleExecutor;

	public CascadingModelBuilder(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);

		this.moduleExecutor = new ModuleExecutor(config);
	}

	@Override
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) {

		ModuleIdentifier identifier = source == null ? null : source.getIdentifier();
		log.info("onRelationship " + identifier + " -> " + outgoing);

	}

	@Override
	protected void onModuleResolved(IRhenaModule resolvedModule) throws RhenaException {

		// we can't build resolved module until all of its outgoing
		// relationships have been build

		// log.info("onModuleResolved: " + resolvedModule.getIdentifier());

		// all entry points are in cache because of the model resolution
		// determine the highest entry point to target this resolved module

		IEntryPoint incoming = null;
		for (IEntryPoint cached : getCache().getEntryPoints()) {
			if (cached.getTarget().equals(resolvedModule.getIdentifier())) {
				// cached entry point targeting module found
				if (incoming == null) {
					incoming = cached;
				} else if (cached.getExecutionType().greaterOrEqualTo(incoming.getExecutionType())) {
					incoming = cached;
				}
			}
		}

		/**
		 * we want all of its outgoing to be already executed before submitting
		 * it for execution
		 */
		// Set<IEntryPoint> outgoing = new HashSet<IEntryPoint>();

		IRhenaBuilder builder = null;
		// log.info("Submitting for execution: " + targeting);
		if (resolvedModule.getModuleType() == EModuleType.WORKSPACE) {

			builder = new WorkspaceBuilder(incoming, resolvedModule);
		} else if (resolvedModule.getModuleType() == EModuleType.REMOTE) {

			builder = new RemoteBuilder(incoming, resolvedModule);
		} else {

			throw new RhenaException("Framework doesn't know how to handle module of type: " + resolvedModule.getModuleType());
		}

		// before submitting,
		moduleExecutor.submit(builder);
	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		return getCache().getCachedExecution(identifier);
	}

}
