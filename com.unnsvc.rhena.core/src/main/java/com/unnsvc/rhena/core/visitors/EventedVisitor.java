
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class EventedVisitor implements IModelVisitor {

	private VisitationHandler handler;
	private EnterType enter;
	private boolean enterUnusedLifecycle = false;

	public EventedVisitor(EnterType enter, VisitationHandler handler) {

		this.handler = handler;
		this.enter = enter;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		if (module.getParentModule() != null) {

			enterTree(module, module.getParentModule());
		}

		// after parent so we can call getLifecycleDecalartion on model, which
		// chains up to parent
		if (handler instanceof ModuleVisitationHandler) {
			((ModuleVisitationHandler) handler).startModule(module);
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

		if (handler instanceof ModuleVisitationHandler) {
			((ModuleVisitationHandler) handler).endModule(module);
		}
	}

	private void processLifecycle(IRhenaModule module, ILifecycleDeclaration lifecycle) throws RhenaException {

		enterTree(module, lifecycle.getContext().getModuleEdge());

		for (IProcessorReference proc : lifecycle.getProcessors()) {

			enterTree(module, proc.getModuleEdge());
		}

		enterTree(module, lifecycle.getGenerator().getModuleEdge());
	}

	private void enterTree(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		if (handler.canEnter(module, edge)) {
			if (enter == EnterType.BEFORE) {
				edge.getTarget().visit(this);
			}

			if (handler instanceof EdgeVisitationHandler) {
				((EdgeVisitationHandler) handler).handleEdge(module, edge);
			}

			if (enter == EnterType.AFTER) {
				edge.getTarget().visit(this);
			}
		}
	}

	public enum EnterType {

		BEFORE, AFTER, NOENTER
	}

	public EventedVisitor setEnterUnusedLifecycle(boolean enterUnusedLifecycle) {

		this.enterUnusedLifecycle = enterUnusedLifecycle;
		return this;
	}
}
