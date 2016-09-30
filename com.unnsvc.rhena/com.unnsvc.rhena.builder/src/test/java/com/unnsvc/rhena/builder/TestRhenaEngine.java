package com.unnsvc.rhena.builder;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.RhenaModelBuilder;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.resolvers.ResolutionEngine;
import com.unnsvc.rhena.builder.resolvers.WorkspaceRepository;

public class TestRhenaEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testEngine() throws Exception {
		
		
		WorkspaceRepository workspaceRepo = new WorkspaceRepository(new File("../example-workspace").getAbsoluteFile());

//		RemoteRepository remoteRepo = new RemoteRepository("file:///home/noname/.rhena/repository");
		
		ResolutionEngine resolution = new ResolutionEngine(workspaceRepo);
		RhenaContext context = new RhenaContext(resolution);
		
		RhenaModule module = new RhenaModelBuilder().buildModel(context, "component1:module1:0.0.1");
	}
}

