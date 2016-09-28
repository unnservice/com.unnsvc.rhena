package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.core.identifier.ProjectIdentifier;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.MavenResolver;
import com.unnsvc.rhena.core.resolution.RepositoryManager;
import com.unnsvc.rhena.core.resolution.ResolutionEngine;
import com.unnsvc.rhena.core.resolution.WorkspaceResolver;

public class TestModelGeneration {

	@Test
	public void testModelGeneration() throws Exception {

		WorkspaceResolver workspaceResolver = new WorkspaceResolver("../example-workspace");
		WorkspaceResolver remoteResolver = new WorkspaceResolver("/home/noname/.rhena/repository");
		MavenResolver mavenResolver = new MavenResolver();
		
		RepositoryManager manager = new RepositoryManager(workspaceResolver, remoteResolver, mavenResolver);
		ResolutionEngine engine = new ResolutionEngine(manager);

		RhenaProject project = engine.generateModel(new ProjectIdentifier("component1:project1:0.0.0"));
	}
}
