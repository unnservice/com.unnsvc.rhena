
package com.unnsvc.rhena.core.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.ExecutionTypeMap;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;
import com.unnsvc.rhena.common.visitors.IModelVisitor;
import com.unnsvc.rhena.core.resolution.Dependencies;

public class URLDependencyTreeVisitor extends ADependencyTreeVisitor {

	private Map<EExecutionType, List<IRhenaExecution>> dependencies;

	public URLDependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType, ESelectionType selectionType) {

		this(cache, requestedType, new ExecutionTypeMap(), selectionType, new HashSet<IRhenaEdge>());
	}

	protected URLDependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType, Map<EExecutionType, List<IRhenaExecution>> dependencies,
			ESelectionType selectionType, Set<IRhenaEdge> edgeTracker) {

		super(cache, requestedType, selectionType, edgeTracker);
		this.dependencies = dependencies;
	}

	@Override
	protected IModelVisitor newVisitor(IRhenaCache cache, EExecutionType executionType, ESelectionType selectionType, Set<IRhenaEdge> edgeTracker) {

		return new URLDependencyTreeVisitor(cache, executionType, dependencies, selectionType, edgeTracker);
	}

	@Override
	public void selectDependency(IRhenaEdge enteringEdge, IRhenaModule enteringModule) {

		EExecutionType type = enteringEdge.getEntryPoint().getExecutionType();
		IRhenaExecution execution = getCache().getExecutions().get(enteringModule.getIdentifier()).get(type);
		dependencies.get(type).add(execution);
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
