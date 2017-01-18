
package com.unnsvc.rhena.core.visitors;

import java.util.Set;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

/**
 * Dependency tree visitor that doesn't actually do anything
 * 
 * @author noname
 *
 */
public abstract class ADependencyTreeVisitor implements IModelVisitor {

	private IRhenaCache cache;
	private EExecutionType type;
	private ESelectionType selectionType;
	private Set<IRhenaEdge> edgeTracker;

	protected ADependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType, ESelectionType selectionType, Set<IRhenaEdge> edgeTracker) {

		this.cache = cache;
		this.type = requestedType;
		this.selectionType = selectionType;
		this.edgeTracker = edgeTracker;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		// visiting this module

		// visit dependencies
		for (IRhenaEdge edge : module.getDependencies()) {

			if(edgeTracker.contains(edge)) {
				continue;
			}
			
			IEntryPoint entryPoint = edge.getEntryPoint();
			if (getType().compareTo(entryPoint.getExecutionType()) >= 0) {
				// for example, we reuqested TEST, and current is MAIN or TEST

				IModelVisitor visitor = newVisitor(cache, edge.getEntryPoint().getExecutionType(), edge.getTraverseType(), edgeTracker);
				IRhenaModule entering = getModule(edge.getEntryPoint().getTarget());
				// Enter tree
				if (selectionType.equals(ESelectionType.SCOPE)) {

					selectDependency(edge, entering);
					entering.visit(visitor);

				} else if (selectionType.equals(ESelectionType.COMPONENT)) {

					if (module.getIdentifier().getComponentName().equals(entering.getIdentifier().getComponentName())) {

						selectDependency(edge, entering);
						entering.visit(visitor);
					}
				} else if (selectionType.equals(ESelectionType.DIRECT)) {

					selectDependency(edge, entering);
					// we don't enter further here
				}
			}
			
			edgeTracker.add(edge);
		}
	}

	protected IRhenaModule getModule(ModuleIdentifier target) {

		return cache.getModule(target);
	}

	/**
	 * Used to narrow the scope while entering dependencies
	 * 
	 * @param cache2
	 * @param executionType
	 * @return
	 */
	protected abstract IModelVisitor newVisitor(IRhenaCache cache, EExecutionType executionType, ESelectionType selectionType, Set<IRhenaEdge> edgeTracker);

	protected abstract void selectDependency(IRhenaEdge enteringEdge, IRhenaModule enteringModule);

	protected IRhenaCache getCache() {

		return cache;
	}

	protected EExecutionType getType() {

		return type;
	}
}
