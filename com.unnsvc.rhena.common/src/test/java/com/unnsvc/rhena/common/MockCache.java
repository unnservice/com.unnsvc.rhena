
package com.unnsvc.rhena.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class MockCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> modules;
	private Map<IEntryPoint, IExecutionResponse> results;
	private Set<IEntryPoint> entryPoints;

	public MockCache() {

		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.results = new HashMap<IEntryPoint, IExecutionResponse>();
		this.entryPoints = new HashSet<IEntryPoint>();
	}

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return modules.get(identifier);
	}

	@Override
	public void cacheModule(IRhenaModule module) {

		modules.put(module.getIdentifier(), module);
	}

	@Override
	public void cacheExecution(IEntryPoint entryPoint, IExecutionResponse result) {

		results.put(entryPoint, result);
	}

	@Override
	public IExecutionResponse getCachedExecution(IEntryPoint entryPoint) {

		return results.get(entryPoint);
	}

	@Override
	public Set<IEntryPoint> getEntryPoints() {

		return entryPoints;
	}

	@Override
	public void cacheEntryPoint(IEntryPoint entryPoint) {

		this.entryPoints.add(entryPoint);
	}

}
