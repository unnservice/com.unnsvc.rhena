
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;
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

	public ADependencyTreeVisitor(IRhenaCache cache, EExecutionType requestedType) {

		this.cache = cache;
		this.type = requestedType;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		// visiting this module

		// visit dependencies
		for (IRhenaEdge edge : module.getDependencies()) {

			if (edge.getEntryPoint().getExecutionType().compareTo(getType()) <= 0) {

				IModelVisitor visitor = newVisitor(cache, edge.getEntryPoint().getExecutionType());
				IRhenaModule entering = getModule(edge.getEntryPoint().getTarget());
				// Enter tree
				if (edge.getTraverseType().equals(ESelectionType.SCOPE)) {

					beforeEnteringEdge(edge, entering);
					entering.visit(visitor);

				} else if (edge.getTraverseType().equals(ESelectionType.COMPONENT)) {

					if (module.getIdentifier().getComponentName().equals(entering.getIdentifier().getComponentName())) {

						beforeEnteringEdge(edge, entering);
						entering.visit(visitor);
					}
				} else if (edge.getTraverseType().equals(ESelectionType.DIRECT)) {

					beforeEnteringEdge(edge, entering);
					// we don't enter further here
				}
			}
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
	protected abstract IModelVisitor newVisitor(IRhenaCache cache, EExecutionType executionType);

	protected abstract void beforeEnteringEdge(IRhenaEdge enteringEdge, IRhenaModule enteringModule);

	protected IRhenaCache getCache() {

		return cache;
	}

	protected EExecutionType getType() {

		return type;
	}
}
