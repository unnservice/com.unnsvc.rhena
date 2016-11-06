
package com.unnsvc.rhena.core;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.core.model.RhenaEdge;

public class RhenaContext implements IRhenaContext {

	private CascadingModelResolver cascadingResolver;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;

	public RhenaContext(IRhenaConfiguration config) {

		this.cascadingResolver = new CascadingModelResolver(config);
		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		IRhenaEdge entryPoint = new RhenaEdge(EExecutionType.MODEL, identifier, TraverseType.SCOPE);
		return cascadingResolver.resolveEdge(entryPoint);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IRhenaExecution execution = null;
		if (executions.containsKey(module.getIdentifier())) {

			if ((execution = executions.get(module.getIdentifier()).get(type)) != null) {
				return execution;
			}
		} else {
			executions.put(module.getIdentifier(), new HashMap<EExecutionType, IRhenaExecution>());
		}
		// produce execution

		execution = module.getRepository().materialiseExecution(this, module, type);
		this.executions.get(module.getIdentifier()).put(type, execution);
		return execution;
	}
}
