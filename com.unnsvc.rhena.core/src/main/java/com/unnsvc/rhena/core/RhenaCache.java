
package com.unnsvc.rhena.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @TODO provide a lock() so that multiple threads can lock on the cache?
 * @author noname
 *
 */
public class RhenaCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> cachedModules;
	private Set<IEntryPoint> entryPoints;
	private Map<IEntryPoint, IExecutionResult> cachedExecutions;

	public RhenaCache() {

		this.cachedModules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.entryPoints = new HashSet<IEntryPoint>();
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
	public Set<IEntryPoint> getEntryPoints() {

		return entryPoints;
	}

	@Override
	public void cacheEntryPoint(IEntryPoint entryPoint) {

		this.entryPoints.add(entryPoint);
	}

	@Override
	public IExecutionResult getCachedExecution(IEntryPoint entryPoint) throws NotFoundException {

		for (IEntryPoint existing : cachedExecutions.keySet()) {

			if (existing.getTarget().equals(entryPoint.getTarget())) {
				if (existing.getExecutionType().greaterOrEqualTo(entryPoint.getExecutionType())) {
					return cachedExecutions.get(existing);
				}
			}
		}
		throw new NotFoundException("Execution not found for: " + entryPoint);
		// return cachedExecutions.get(entryPoint);
	}

	@Override
	public void cacheExecution(IEntryPoint entryPoint, IExecutionResult result) {

		this.cachedExecutions.put(entryPoint, result);
	}
}
