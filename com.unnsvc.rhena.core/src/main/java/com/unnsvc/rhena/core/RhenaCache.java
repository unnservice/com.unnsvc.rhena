
package com.unnsvc.rhena.core;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent;
import com.unnsvc.rhena.core.events.ModuleAddRemoveEvent.EAddRemove;

public class RhenaCache implements IRhenaCache {

	private IRhenaContext context;
	private Map<ModuleIdentifier, IRhenaModule> modules;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private Map<ModuleIdentifier, ILifecycle> lifecycles;

	public RhenaCache(IRhenaContext context) {

		this.context = context;
		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		// @TODO evaluate if we can use CompletionService and some .take().get()
		// trickery for efficient execution loop
		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		this.lifecycles = new HashMap<ModuleIdentifier, ILifecycle>();
	}

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return modules.get(identifier);
	}

	@Override
	public void addModule(ModuleIdentifier identifier, IRhenaModule module) throws RhenaException {

		context.getListenerConfig().fireListener(new ModuleAddRemoveEvent(identifier, EAddRemove.ADDED));
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

	@Override
	public Map<ModuleIdentifier, ILifecycle> getLifecycles() {

		return lifecycles;
	}

	@Override
	public ILifecycle getLifecycle(ModuleIdentifier identifier) {

		return lifecycles.get(identifier);
	}
}
