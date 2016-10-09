
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.resolution.ResolutionManager;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.RhenaEdgeProcessingVisitor;
import com.unnsvc.rhena.core.visitors.RhenaEdgeProcessingVisitor.EdgeCallback;
import com.unnsvc.rhena.core.visitors.RhenaModelProcessingVisitor;
import com.unnsvc.rhena.core.visitors.RhenaModelProcessingVisitor.ModuleCallback;

public class TestRhenaModule {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("component1:module1:0.0.1");

		WorkspaceRepository workspace = new WorkspaceRepository(new File("../example-workspace"));
		ResolutionManager resolution = new ResolutionManager(workspace);

		RhenaModel model = resolution.materialiseModel(entryPointIdentifier);

		// model.visit(new ModelResolutionVisitor(resolution));
		// model.visit(new ModelMergeVisitor(resolution));

		// model.visit(new LoggingVisitor(resolution));
		
		model.visit(new RhenaModelProcessingVisitor(resolution, new ModuleCallback() {
			
			@Override
			public void onModel(RhenaModel model) {
				
				log.debug("Model node: " + model);
			}
		}));

		model.visit(new RhenaEdgeProcessingVisitor(resolution, new EdgeCallback() {

			@Override
			public void onModelEdge(RhenaEdge edge) {
				log.debug("On edge: " + edge);
			}
		}));

		// model.visit(new LifecycleMaterialisingVisitor(resolution,
		// ModuleState.RESOLVED));

		// resolution.materialiseState(entryPointIdentifier,
		// ModuleState.TESTED);

		// RhenaLifecycleExecution modelExecution =
		// modelMaterialiser.materialiseScope(CompositeScope.ITEST, model);

	}
}
