
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

	public EdgeVisitor(EdgeHandler handler, EnterType enter) {

		this.handler = handler;
		this.enter = enter;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		if (module.getParentModule() != null) {

			enterTree(module.getParentModule());
		}

		if (module.getLifecycleName() != null) {

			ILifecycleDeclaration lifecycle = module.getLifecycleDeclaration(module.getLifecycleName());

			enterTree(lifecycle.getConfigurator());

			for (IProcessorReference proc : lifecycle.getProcessors()) {

				enterTree(proc);
			}

			enterTree(lifecycle.getGenerator());
		}

		for (IRhenaEdge edge : module.getDependencyEdges()) {

			enterTree(edge);
		}

	}

	private void enterTree(IRhenaEdge parentModule) throws RhenaException {

		if (enter == EnterType.BEFORE) {
			parentModule.getTarget().visit(this);
		}

		handler.handleEdge(parentModule);

		if (enter == EnterType.AFTER) {
			parentModule.getTarget().visit(this);
		}
	}

	public interface EdgeHandler {

		public void handleEdge(IRhenaEdge edge);
	}

	public enum EnterType {

		BEFORE, AFTER, NOENTER
	}
}
