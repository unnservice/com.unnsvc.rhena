
package com.unnsvc.rhena.ng;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.ng.model.RhenaModule;
import com.unnsvc.rhena.ng.model.visitors.ModelInitialisationVisitor;
import com.unnsvc.rhena.ng.model.visitors.ModelMergeVisitor;
import com.unnsvc.rhena.ng.model.visitors.LoggingVisitor;
import com.unnsvc.rhena.ng.resolution.ResolutionManager;
import com.unnsvc.rhena.ng.resolution.RhenaModelMaterialiser;
import com.unnsvc.rhena.ng.resolution.WorkspaceRepository;

public class TestRhenaModule {

	private Logger log = LoggerFactory.getLogger(getClass());

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

		RhenaLifecycleExecution modelExecution = modelMaterialiser.materialiseScope(CompositeScope.ITEST, model);

	}
}
