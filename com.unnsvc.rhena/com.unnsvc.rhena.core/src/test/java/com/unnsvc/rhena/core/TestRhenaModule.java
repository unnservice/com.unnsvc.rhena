
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.core.resolution.RhenaResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.ModelBuildingVisitor;
import com.unnsvc.rhena.core.visitors.ModelInitialisationVisitor;

public class TestRhenaModule {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("component1:module1:0.0.1");

		IResolutionContext context = new RhenaResolutionContext();
		context.getRepositories().add(new WorkspaceRepository(context, new File("../example-workspace")));

		RhenaModel model = context.materialiseModel(entryPointIdentifier);

		model.visit(new ModelInitialisationVisitor(context));

		// model.visit(new LoggingVisitor(resolution));

		model.visit(new ModelBuildingVisitor(context));

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
