
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.resolution.RhenaResolver;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.ModelInitialisationVisitor;

public class TestRhenaModule {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("component1:module1:0.0.1");

		WorkspaceRepository workspace = new WorkspaceRepository(new File("../example-workspace"));
		IResolver resolver = new RhenaResolver(workspace);
		RhenaModel model = resolver.materialiseModel(entryPointIdentifier);

		// model.visit(new ModelMergeVisitor(resolution));
		model.visit(new ModelInitialisationVisitor(resolver));

		// model.visit(new LoggingVisitor(resolution));
		
//		model.visit(new RhenaModelProcessingVisitor(resolver, new ModuleCallback() {
//			
//			@Override
//			public void onModel(RhenaModel model) {
//				
////				log.debug("Model node: " + model);
//			}
//		}));
//
//		model.visit(new RhenaEdgeProcessingVisitor(resolver, new EdgeCallback() {
//
//			@Override
//			public void onModelEdge(RhenaEdge edge) {
////				log.debug("On edge: " + edge);
//			}
//		}));

		// model.visit(new LifecycleMaterialisingVisitor(resolution,
		// ModuleState.RESOLVED));

		// resolution.materialiseState(entryPointIdentifier,
		// ModuleState.TESTED);

		// RhenaLifecycleExecution modelExecution =
		// modelMaterialiser.materialiseScope(CompositeScope.ITEST, model);

	}
}
