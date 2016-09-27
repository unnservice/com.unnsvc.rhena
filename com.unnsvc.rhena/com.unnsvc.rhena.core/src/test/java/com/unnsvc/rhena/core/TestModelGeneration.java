package com.unnsvc.rhena.core;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.LocationResolver;

public class TestModelGeneration {

	@Test
	public void testModelGeneration() throws Exception {

		LocationResolver workspaceResolver = new LocationResolver(new URI("file://../com.unnsvc.rhena/example-workspace"));
		LocationResolver remoteResolver = new LocationResolver(new URI("file:///home/noname/.rhena/repository"));

		RhenaEngine engine = new RhenaEngine(workspaceResolver, remoteResolver);
		RhenaProject project = engine.createModel("com.test.one", "com.test.one.project1", "0.0.1");
		Assert.assertNotNull(project);
	}
}
