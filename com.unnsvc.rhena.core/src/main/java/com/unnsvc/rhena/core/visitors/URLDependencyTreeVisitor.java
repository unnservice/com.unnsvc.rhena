
package com.unnsvc.rhena.core.visitors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.ExecutionTypeMap;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;
import com.unnsvc.rhena.common.visitors.IModelVisitor;
import com.unnsvc.rhena.core.resolution.Dependencies;

public class URLDependencyTreeVisitor extends ADependencyTreeVisitor {

	private Map<EExecutionType, List<IRhenaExecution>> dependencies;

	public URLDependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType) {

		this(cache, requestedType, new ExecutionTypeMap());
	}

	public URLDependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType, Map<EExecutionType, List<IRhenaExecution>> dependencies) {

		super(cache, requestedType);
		this.dependencies = dependencies;
	}

	@Override
	protected IModelVisitor newVisitor(IRhenaCache cache, EExecutionType executionType) {

		return new URLDependencyTreeVisitor(cache, executionType, dependencies);
	}

	@Override
	public void beforeEnteringEdge(IRhenaEdge enteringEdge, IRhenaModule enteringModule) {

		if (!dependencies.containsKey(enteringEdge.getEntryPoint().getExecutionType())) {

			dependencies.put(getType(), new ArrayList<IRhenaExecution>());
		}

		IRhenaExecution execution = getCache().getExecutions().get(enteringModule.getIdentifier()).get(getType());
		dependencies.get(getType()).add(execution);
	}

	public List<IRhenaExecution> getExecutions(EExecutionType type) {

		List<IRhenaExecution> depchain = dependencies.get(type);
		if (depchain == null) {

			return new ArrayList<IRhenaExecution>();
		}
		return depchain;
	}

	public IDependencies getDependencies() {

		return new Dependencies(getType(), dependencies);
	}
}
