package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.RepositoryManager;
import com.unnsvc.rhena.core.resolution.WorkspaceRepositoryResolver;
import com.unnsvc.rhena.core.resolution.ResolutionContext;
import com.unnsvc.rhena.core.resolution.ResolutionEngine;

public class TestModelGeneration {

	@Test
	public void testModelGeneration() throws Exception {

		WorkspaceRepositoryResolver workspaceResolver = new WorkspaceRepositoryResolver("../example-workspace");
		WorkspaceRepositoryResolver remoteResolver = new WorkspaceRepositoryResolver("/home/noname/.rhena/repository");
		RepositoryManager manager = new RepositoryManager(workspaceResolver, remoteResolver);
		ResolutionEngine engine = new ResolutionEngine(manager);

		ResolutionContext context = new ResolutionContext(engine);
		
		ProjectIdentifier projectIdentifier = QualifiedIdentifier.valueOfProject("component1:project1:0.0.0");
		RhenaProject project = engine.resolveModel(context, projectIdentifier);

		// RhenaEngine engine = new RhenaEngine(workspaceResolver,
		// remoteResolver);
		// RhenaProject project = engine.createModel("com.test.one",
		// "com.test.one.project1", "0.0.1");
		// Assert.assertNotNull(project);
	}
}

