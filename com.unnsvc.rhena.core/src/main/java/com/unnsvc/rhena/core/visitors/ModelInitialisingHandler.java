
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.ModuleType;

public class ModelInitialisingHandler implements EdgeVisitationHandler, ModuleVisitationHandler {

	private IRhenaContext context;

	public ModelInitialisingHandler(IRhenaContext context) {

		this.context = context;
	}

	@Override
	public void handleEdge(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		IRhenaModule target = context.materialiseModel(edge.getTarget().getModuleIdentifier());
		edge.setTarget(target);
	}

	@Override
	public void startModule(IRhenaModule module) throws RhenaException {

		// // we create the default lifecycles here, and they will be resolved
		// as
		// // the visitor enters them
		// if
		// (!module.hasLifecycleDeclaration(RhenaConstants.DEFAULT_LIFECYCLE_NAME))
		// {
		//
		// ExecutionType et = ExecutionType.FRAMEWORK;
		// TraverseType tt = TraverseType.SCOPE;
		//
		// // @TODO must know the current version to set the lifecycle version
		// // of the runtime accordingly
		// RhenaReference lifecycleReference = new
		// RhenaReference(ModuleIdentifier.valueOf("com.unnsvc.rhena:lifecycle:0.0.1"));
		// LifecycleDeclaration lifecycle = new LifecycleDeclaration("default");
		//
		// lifecycle.setContext(new ContextReference(lifecycleReference,
		// DefaultContext.class.getName(), null, Utils.newEmptyDocument(), et,
		// tt));
		// lifecycle.addProcessor(new ProcessorReference(lifecycleReference,
		// DefaultProcessor.class.getName(), null, Utils.newEmptyDocument(), et,
		// tt));
		// lifecycle.setGenerator(new GeneratorReference(lifecycleReference,
		// DefaultGenerator.class.getName(), null, Utils.newEmptyDocument(), et,
		// tt));
		//
		// module.getLifecycleDeclarations().put(RhenaConstants.DEFAULT_LIFECYCLE_NAME,
		// lifecycle);
		// }
	}

	@Override
	public void endModule(IRhenaModule module) {

	}

	@Override
	public boolean canEnter(IRhenaModule module, IRhenaEdge edge) throws RhenaException {

		if (edge.getTarget().getModuleType() == ModuleType.REFERENCE) {

			return true;
		}
		return false;
	}

}
