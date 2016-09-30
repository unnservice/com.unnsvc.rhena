package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.model.Scope;
import com.unnsvc.rhena.core.resolution.MavenResolver;
import com.unnsvc.rhena.core.resolution.ResolutionEngine;
import com.unnsvc.rhena.core.resolution.RhenaRepositoryManager;
import com.unnsvc.rhena.core.resolution.WorkspaceResolver;

public class TestModelGeneration {

	@Test
	public void testModelGeneration() throws Exception {

		WorkspaceResolver workspaceResolver = new WorkspaceResolver("../example-workspace");
		WorkspaceResolver remoteResolver = new WorkspaceResolver("/home/noname/.rhena/repository");
		MavenResolver mavenResolver = new MavenResolver();
		
		RhenaRepositoryManager manager = new RhenaRepositoryManager(workspaceResolver, remoteResolver, mavenResolver);
		ResolutionEngine engine = new ResolutionEngine(manager);

		RhenaProject project = engine.generateModel("component1:0.0.1", "project1", Scope.ITEST);
	}
}
