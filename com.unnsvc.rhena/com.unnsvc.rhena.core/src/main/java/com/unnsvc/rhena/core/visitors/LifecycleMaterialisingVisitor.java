
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.resolution.RhenaResolver;

public class LifecycleMaterialisingVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaResolver resolution;
	private ModuleState moduleState;

	public LifecycleMaterialisingVisitor(RhenaResolver resolution, ModuleState moduleState) {

		this.resolution = resolution;
		this.moduleState = moduleState;
	}

	@Override
	public void startModel(RhenaModel module) throws RhenaException {

		if (module.getLifecycleModule() != null) {
			RhenaModel lifecycleModel = resolution.materialiseModel(module.getLifecycleModule().getTarget());
			lifecycleModel.visit(new LifecycleMaterialisingVisitor(resolution, ModuleState.RESOLVED));
		}

		for (RhenaEdge edge : module.getDependencyEdges()) {

			RhenaModel dependency = resolution.materialiseModel(edge.getTarget());
			dependency.visit(new LifecycleMaterialisingVisitor(resolution, ModuleState.RESOLVED));
		}
	}

	/**
	 * Here we perform the actual materialisation of the lifecycle
	 */
	@Override
	public void endModel(RhenaModel module) throws RhenaException {

		// RhenaLifecycleExecution execution =
		// context.getResolutionManager().materialiseState(module.getModuleIdentifier(),
		// ModuleState.DEPLOYED);
		// log.trace("[" + module.getModuleIdentifier() + "]:" + moduleState + "
		// Produced: " + execution);

//		log.info("materialising [" + module.getModuleIdentifier() + "] " + moduleState.toLabel());
//		resolution.materialiseState(module.getModuleIdentifier(), moduleState);
		
		switch(moduleState) {
			case UNRESOLVED:
			case MODEL:
				resolution.materialiseModel(module.getModuleIdentifier());
				break;
			case RESOLVED:
		}
	}

}
