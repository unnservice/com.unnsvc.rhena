
package com.unnsvc.rhena.common.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.unnsvc.rhena.common.ExecutionTypeMap;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class ExecutionCollectionDependencyVisitor extends ADependencyVisitor {

	private ExecutionTypeMap dependencies;

	public ExecutionCollectionDependencyVisitor(IRhenaCache cache, EExecutionType requestedType, ESelectionType selectionType) {

		this(cache, requestedType, new ExecutionTypeMap(), selectionType, new HashSet<IRhenaEdge>());
	}

	protected ExecutionCollectionDependencyVisitor(IRhenaCache cache, EExecutionType requestedType, ExecutionTypeMap dependencies, ESelectionType selectionType,
			Set<IRhenaEdge> edgeTracker) {

		super(cache, requestedType, selectionType, edgeTracker);
		this.dependencies = dependencies;
	}

	@Override
	protected IModelVisitor newVisitor(IRhenaCache cache, EExecutionType executionType, ESelectionType selectionType, Set<IRhenaEdge> edgeTracker) {

		return new ExecutionCollectionDependencyVisitor(cache, executionType, dependencies, selectionType, edgeTracker);
	}

	@Override
	public void selectDependency(IRhenaEdge enteringEdge, IRhenaModule enteringModule) {

		EExecutionType type = enteringEdge.getEntryPoint().getExecutionType();
		IRhenaExecution execution = getCache().getExecutions().get(enteringModule.getIdentifier()).get(type);
		if (!dependencies.get(type).contains(execution)) {
			dependencies.get(type).add(execution);
		}
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
