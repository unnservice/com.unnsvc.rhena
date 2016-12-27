
package com.unnsvc.rhena.core;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RhenaCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> modules;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;

	public RhenaCache() {

		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		// @TODO evaluate if we can use CompletionService and some .take().get()
		// trickery for efficient execution loop
		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
	}

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return modules.get(identifier);
	}

	@Override
	public void addModule(ModuleIdentifier identifier, IRhenaModule module) {

		this.modules.put(identifier, module);
	}

	@Override
	public Map<ModuleIdentifier, IRhenaModule> getModules() {

		return modules;
	}

	@Override
	public Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> getExecutions() {

		return executions;
	}

	@Override
	public Map<EExecutionType, IRhenaExecution> getExecution(ModuleIdentifier identifier) {

		Map<EExecutionType, IRhenaExecution> existing = executions.get(identifier);
		return existing == null ? new HashMap<EExecutionType, IRhenaExecution>() : existing;
	}

	/**
	 * Atmic operation
	 */
	@Override
	public boolean containsExecution(ModuleIdentifier moduleIdentifier, EExecutionType eExecutionType) {

		if (executions.containsKey(moduleIdentifier)) {
			if (executions.get(moduleIdentifier).containsKey(eExecutionType)) {
				return true;
			}
		}
		return false;
	}
}
