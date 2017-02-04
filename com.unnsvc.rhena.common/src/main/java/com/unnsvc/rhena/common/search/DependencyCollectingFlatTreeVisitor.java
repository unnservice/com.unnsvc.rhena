
package com.unnsvc.rhena.common.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.IEntryPoint;

public class DependencyCollectingFlatTreeVisitor extends AFlatTreeVisitor {

	private Map<EExecutionType, List<ModuleIdentifier>> collected;

	public DependencyCollectingFlatTreeVisitor(ILogger logger, IRhenaCache cache) {

		super(logger, cache);
		this.collected = new HashMap<EExecutionType, List<ModuleIdentifier>>();
	}

	@Override
	protected void onAllResolvedEntryPoints(List<IEntryPoint> resolvedEntryPoints) {

		for (IEntryPoint entryPoint : resolvedEntryPoints) {

			addResolvedDependency(entryPoint.getExecutionType(), entryPoint.getTarget());
		}
	}

	private void addResolvedDependency(EExecutionType executionType, ModuleIdentifier identifier) {

		if (executionType.hasParent()) {
			addResolvedDependency(executionType.getParent(), identifier);
		}

		if (!collected.containsKey(executionType)) {
			collected.put(executionType, new ArrayList<ModuleIdentifier>());
		}
		collected.get(executionType).add(identifier);
	}
}
