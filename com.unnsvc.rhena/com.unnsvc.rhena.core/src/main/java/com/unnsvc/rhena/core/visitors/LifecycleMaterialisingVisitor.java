
package com.unnsvc.rhena.core.visitors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.DependencyType;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.common.model.RhenaModuleEdge;
import com.unnsvc.rhena.core.resolution.RhenaModelMaterialiser;

public class LifecycleMaterialisingVisitor implements IVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModelMaterialiser materialiser;
	private DependencyType dependencyType;

	public LifecycleMaterialisingVisitor(RhenaModelMaterialiser materialiser, DependencyType dependencyType) {

		this.materialiser = materialiser;
		this.dependencyType = dependencyType;
	}

	@Override
	public void startModule(RhenaModule module) throws RhenaException {

		if (module.getLifecycleDeclaration() != null) {
			RhenaModule lifecycleModel = materialiser.materialiseModel(module.getLifecycleDeclaration());
			lifecycleModel.visit(new LifecycleMaterialisingVisitor(materialiser, DependencyType.COMPILE));
		}

		for (RhenaModuleEdge edge : module.getDependencyEdges()) {

			RhenaModule dependency = materialiser.materialiseModel(edge.getTarget());
			dependency.visit(new LifecycleMaterialisingVisitor(materialiser, edge.getDependencyType()));
		}
	}

	/**
	 * Here we perform the actual materialisation of the lifecycle
	 */
	@Override
	public void endModule(RhenaModule module) throws RhenaException {

		materialiseScope(module);
	}

	private void materialiseScope(RhenaModule module) throws RhenaException {

		RhenaLifecycleExecution execution = module.getRepository().materialisePackaged(module);
		log.trace("[" + module.getModuleIdentifier() + "]:" + dependencyType + " Produced: " + execution);
	}

}
