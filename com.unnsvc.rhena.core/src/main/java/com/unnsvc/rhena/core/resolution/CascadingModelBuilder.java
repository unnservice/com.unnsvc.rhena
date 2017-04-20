
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
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
	private ModuleExecutor moduleExecutor;

	public CascadingModelBuilder(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);
		this.moduleExecutor = new ModuleExecutor(config, cache);
	}

	@Override
	protected void onModuleResolved(IRhenaModule resolvedModule) throws RhenaException {

		log.info("onModuleResolved: " + resolvedModule.getIdentifier());

		// all entry points are in cache because of the model resolution
		// determine the highest entry point to target this resolved module
		IEntryPoint targeting = null;
		for (IEntryPoint cached : getCache().getEntryPoints()) {
			if (cached.getTarget().equals(resolvedModule.getIdentifier())) {
				if (targeting == null) {
					targeting = cached;
				} else if (cached.getExecutionType().greaterOrEqualTo(targeting.getExecutionType())) {
					targeting = cached;
				}
			}
		}

		log.info("Submitting for execution: " + targeting);
		if (resolvedModule.getModuleType() == EModuleType.WORKSPACE) {

			moduleExecutor.submit(new WorkspaceBuilder(targeting.getExecutionType(), resolvedModule));
		} else if (resolvedModule.getModuleType() == EModuleType.REMOTE) {

			moduleExecutor.submit(new RemoteBuilder(targeting.getExecutionType(), resolvedModule));
		} else {
			throw new RhenaException("Framework doesn't know how to handle module of type: " + resolvedModule.getModuleType());
		}
	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		return getCache().getCachedExecution(identifier);
	}

}
