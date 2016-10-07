
package com.unnsvc.rhena.builder;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;
import com.unnsvc.rhena.builder.resolvers.ResolutionContext;
import com.unnsvc.rhena.builder.resolvers.WorkspaceRepository;
import com.unnsvc.rhena.builder.visitors.BuildingVisitor;

public class TestRhenaEngine {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testEngine() throws Exception {

		WorkspaceRepository workspaceRepo = new WorkspaceRepository(new File("../example-workspace").getAbsoluteFile());

		// RemoteRepository remoteRepo = new
		// RemoteRepository("file:///home/noname/.rhena/repository");

		ResolutionContext resolution = new ResolutionContext(workspaceRepo);
		RhenaContext context = new RhenaContext(resolution);

//		RhenaModuleDescriptor descriptor = resolution.requestScope(CompositeScope.MODEL, new ModuleIdentifier("component1", "module1", "0.0.1"));
//		descriptor.buildModule(CompositeScope.ITEST, context);
		
//		RhenaModuleDescriptor descriptor = context.getResolution().resolveDescriptor(new ModuleIdentifier("component1", "module1", "0.0.1"));
		
		
		RhenaModuleDescriptor descriptor = context.getResolution().materialiseScope(context, CompositeScope.MODEL, new ModuleIdentifier("component1", "module1", "0.0.1"));
		
//		RhenaModuleDescriptor descriptor = context.getResolution().resolveDescriptor(new ModuleIdentifier("component1", "module1", "0.0.1"));
		descriptor.visit(new BuildingVisitor(context, CompositeScope.ITEST));
		
		
		// RhenaModule module = new RhenaModelBuilder().buildModel(context,
		// "component1", "module1", "0.0.1");
		//
		// module.visit(CompositeScope.ITEST, context, new BuildingVisitor());

		// RhenaModuleDescriptor module =
		// resolution.requestScope(CompositeScope.ITEST, new
		// ModuleIdentifier("component1", "module1", "0.0.1"));

	}
}
