
package com.unnsvc.rhena.core;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RhenaCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> cachedModules;
	private Map<IEntryPoint, IExecutionResult> cachedExecutions;

	public RhenaCache() {

		this.cachedModules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.cachedExecutions = new HashMap<IEntryPoint, IExecutionResult>();
	}

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return cachedModules.get(identifier);
	}

	@Override
	public void cacheModule(IRhenaModule module) throws RhenaException {

		this.cachedModules.put(module.getIdentifier(), module);
	}

	@Override
	public void cacheExecution(IEntryPoint entryPoint, IExecutionResult result) {

		this.cachedExecutions.put(entryPoint, result);
	}

	@Override
	public IExecutionResult getCachedExecution(IEntryPoint entryPoint) {

		return cachedExecutions.get(entryPoint);
	}
}
