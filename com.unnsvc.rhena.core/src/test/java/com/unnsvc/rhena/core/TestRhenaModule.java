
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.execution.GraphResolver;
import com.unnsvc.rhena.core.execution.ParallelGraphProcessor;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

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

		// creaate a rhenacontext which will also have event listeners, use
		// listeners for sending logging events? For now just keep it in the resolution context

		// not sure how to pass this context around, it will keep track of
		// everything that we save from the model and execution so that it can
		// be accessed at a common place

		RhenaConfiguration config = new RhenaConfiguration();
		IRhenaContext context = new CachingResolutionContext(config);
		
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../com.unnsvc.erhena/")));
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../")));

		IRhenaModule model = context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1"));

		EExecutionType type = EExecutionType.PROTOTYPE;
		IRhenaEdge entryPointEdge = new RhenaEdge(type, model, TraverseType.SCOPE);
		
		GraphResolver graphResovler = new GraphResolver(context);
		graphResovler.resolveReferences(entryPointEdge);
		new ParallelGraphProcessor(context).processEdges(context.getEdges());

	}

}
