
package com.unnsvc.rhena.common;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class MockCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> modules;
	private Map<IEntryPoint, IExecutionResult> results;

	public MockCache() {

		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.results = new HashMap<IEntryPoint, IExecutionResult>();
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
	public void cacheExecution(IEntryPoint entryPoint, IExecutionResult result) {

		results.put(entryPoint, result);
	}

	@Override
	public IExecutionResult getCachedExecution(IEntryPoint entryPoint) {

		return results.get(entryPoint);
	}

}
