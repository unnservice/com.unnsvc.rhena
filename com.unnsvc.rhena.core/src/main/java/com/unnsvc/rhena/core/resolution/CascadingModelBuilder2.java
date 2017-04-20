
package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelBuilder2 extends AbstractCachingResolver {

	private Logger log = LoggerFactory.getLogger(getClass());

	public CascadingModelBuilder2(IRhenaConfiguration config, IRhenaCache cache, IRhenaResolver resolver) {

		super(cache, resolver);
	}

	@Override
	protected void onModuleResolved(IRhenaModule resolvedModule) {

		log.info("onModuleResolved: " + resolvedModule.getIdentifier());

		// all entry points are in cache because of the model resolution
		// determine the highest entry point to target this resolved module
		IEntryPoint targeting = null;
		for(IEntryPoint cached : getCache().getEntryPoints()) {
			if(cached.getTarget().equals(resolvedModule.getIdentifier())) {
				if(targeting == null) {
					targeting = cached;
				} else if(cached.getExecutionType().greaterOrEqualTo(targeting.getExecutionType())) {
					targeting = cached;
				}
			}
		}
		
		log.info("Submitting for execution: " + targeting);
	}

	public IExecutionResult executeModel(EExecutionType type, ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(type, identifier);

		visitTree(entryPoint, ESelectionType.SCOPE);

		return getCache().getCachedExecution(entryPoint);
	}

}
