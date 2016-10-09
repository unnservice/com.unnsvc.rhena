
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.resolution.ResolutionManager;

public class LifecycleMaterialisingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResolutionManager resolution;
	private ModuleState moduleState;

	public LifecycleMaterialisingVisitor(ResolutionManager resolution, ModuleState moduleState) {

		this.resolution = resolution;
		this.moduleState = moduleState;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if (module.getLifecycleDeclaration() != null) {
			RhenaModule lifecycleModel = resolution.materialiseState(module.getLifecycleDeclaration(), ModuleState.MODEL);
			lifecycleModel.visit(new LifecycleMaterialisingVisitor(resolution, ModuleState.COMPILED));
		}

		for (RhenaModuleEdge edge : module.getDependencyEdges()) {

			RhenaModule dependency = resolution.materialiseState(edge.getTarget(), ModuleState.MODEL);
			dependency.visit(new LifecycleMaterialisingVisitor(resolution, ModuleState.PACKAGED));
		}
	}

	/**
	 * Here we perform the actual materialisation of the lifecycle
	 */
	@Override
	public void endModule(RhenaModule module) throws RhenaException {

//		RhenaLifecycleExecution execution = context.getResolutionManager().materialiseState(module.getModuleIdentifier(), ModuleState.DEPLOYED);
//		log.trace("[" + module.getModuleIdentifier() + "]:" + moduleState + " Produced: " + execution);
	}

}
