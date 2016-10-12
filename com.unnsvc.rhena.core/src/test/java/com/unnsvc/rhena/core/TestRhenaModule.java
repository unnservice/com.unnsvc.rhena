
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.resolution.RhenaResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.EdgeVisitor;
import com.unnsvc.rhena.core.visitors.EdgeVisitor.EdgeHandler;
import com.unnsvc.rhena.core.visitors.EdgeVisitor.EnterType;
import com.unnsvc.rhena.core.visitors.ModelResolutionVisitor;

public class TestRhenaModule {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		long start = System.currentTimeMillis();
		execute();
		long end = System.currentTimeMillis();

		/**
		 * @TODO This is where metrics come into play, implement metrics
		 */
		log.info("Executed in " + (end - start) + "ms (core: Xms, lifecycles: Xms)");
	}

	private void execute() throws Exception {

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("com.unnsvc.ide:common:0.0.1");

		IResolutionContext context = new RhenaResolutionContext();
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../com.unnsvc.ide/")));
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../")));

		IRhenaModule model = context.materialiseModel(entryPointIdentifier);

		model.visit(new ModelResolutionVisitor(context));
		model.visit(new EdgeVisitor(new EdgeHandler() {

			@Override
			public void handleEdge(IRhenaEdge edge) {
				
				System.err.println("Visiting edge: " + edge);
			}
		}, EnterType.BEFORE));

		// model.visit(new ModelInitialisingVisitor(context));
		// model.visit(new ModelMergeVisitor(context));
		// model.visit(new LoggingVisitor(context));
		// model.visit(new ModelBuildingVisitor(context));

		context.materialiseExecution(model, ExecutionType.DELIVERABLE);
	}
}

// Could do something like...
// model.visit(new RhenaModelProcessingVisitor(resolver, new ModuleCallback() {
//
// @Override
// public void onModel(RhenaModel model) {
//
//// log.debug("Model node: " + model);
// }
// }));
//
// model.visit(new RhenaEdgeProcessingVisitor(resolver, new EdgeCallback() {
//
// @Override
// public void onModelEdge(RhenaEdge edge) {
//// log.debug("On edge: " + edge);
// }
// }));
