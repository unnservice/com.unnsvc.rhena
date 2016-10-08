
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;

import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.resolution.ResolutionManager;
import com.unnsvc.rhena.core.resolution.RhenaModelMaterialiser;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.LifecycleMaterialisingVisitor;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;
import com.unnsvc.rhena.core.visitors.ModelInitialisationVisitor;
import com.unnsvc.rhena.core.visitors.ModelMergeVisitor;

public class TestRhenaModule {

//	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		WorkspaceRepository workspace = new WorkspaceRepository(new File("../example-workspace"));
		ResolutionManager resolution = new ResolutionManager(workspace);
		RhenaContext context = new RhenaContext(resolution);

		RhenaModelMaterialiser modelMaterialiser = new RhenaModelMaterialiser(context);
		RhenaModule model = modelMaterialiser.materialiseModel(ModuleIdentifier.valueOf("component1:module1:0.0.1"));

		model.visit(new ModelInitialisationVisitor(modelMaterialiser));
		model.visit(new ModelMergeVisitor(modelMaterialiser));
		
		model.visit(new LoggingVisitor(modelMaterialiser));
		
		
		model.visit(new LifecycleMaterialisingVisitor(modelMaterialiser, CompositeScope.MODEL));
		

//		RhenaLifecycleExecution modelExecution = modelMaterialiser.materialiseScope(CompositeScope.ITEST, model);

	}
}
