
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;

import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.resolution.ResolutionManager;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;
import com.unnsvc.rhena.core.visitors.ModelInitialisationVisitor;
import com.unnsvc.rhena.core.visitors.ModelMergeVisitor;

public class TestRhenaModule {

//	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {
		
		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("component1:module1:0.0.1");
		
		WorkspaceRepository workspace = new WorkspaceRepository(new File("../example-workspace"));
		ResolutionManager resolution = new ResolutionManager(workspace);

		RhenaModule model = resolution.materialiseState(entryPointIdentifier, ModuleState.MODEL);

		model.visit(new ModelInitialisationVisitor(resolution));
		model.visit(new ModelMergeVisitor(resolution));
		
		model.visit(new LoggingVisitor(resolution));
		
		
//		model.visit(new LifecycleMaterialisingVisitor(modelMaterialiser, DependencyType.COMPILE));
		
		resolution.materialiseState(entryPointIdentifier, ModuleState.TESTED);

//		RhenaLifecycleExecution modelExecution = modelMaterialiser.materialiseScope(CompositeScope.ITEST, model);
		


	}
}
