
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class EdgeVisitor implements IModelVisitor {

	private EdgeHandler handler;
	private EnterType enter;
	private boolean enterUnusedLifecycle = false;

	public EdgeVisitor(EnterType enter, EdgeHandler handler) {

		this.handler = handler;
		this.enter = enter;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		if (module.getParentModule() != null) {

			enterTree(module, module.getParentModule());
		}

		if (enterUnusedLifecycle) {

			for (String lifecycleName : module.getLifecycleDeclarations().keySet()) {
				processLifecycle(module, module.getLifecycleDeclaration(lifecycleName));
			}
		} else {
			if (module.getLifecycleName() != null) {
				processLifecycle(module, module.getLifecycleDeclaration(module.getLifecycleName()));
			}
		}

		for (IRhenaEdge edge : module.getDependencyEdges()) {

			enterTree(module, edge);
		}

	}

	private void processLifecycle(IRhenaModule module, ILifecycleDeclaration lifecycle) throws RhenaException {

		enterTree(module, lifecycle.getConfigurator());

		for (IProcessorReference proc : lifecycle.getProcessors()) {

			enterTree(module, proc);
		}

		enterTree(module, lifecycle.getGenerator());
	}

	private void enterTree(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		if (handler.canEnter(module, edge)) {
			if (enter == EnterType.BEFORE) {
				edge.getTarget().visit(this);
			}

			handler.handleEdge(module, edge);

			if (enter == EnterType.AFTER) {
				edge.getTarget().visit(this);
			}
		}
	}

	public interface EdgeHandler {

		public void handleEdge(IRhenaModule module, IRhenaEdge edge) throws RhenaException;

		public boolean canEnter(IRhenaModule module, IRhenaEdge edge) throws RhenaException;
	}

	public enum EnterType {

		BEFORE, AFTER, NOENTER
	}

	public EdgeVisitor setEnterUnusedLifecycle(boolean enterUnusedLifecycle) {

		this.enterUnusedLifecycle = enterUnusedLifecycle;
		return this;
	}
}
