
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
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
		// listeners for sending logging events?

		// not sure how to pass this context around, it will keep track of
		// everything that we save from the model and execution so that it can
		// be accessed at a common place
		IRhenaContext context = new RhenaContext();

		RhenaConfiguration config = new RhenaConfiguration();

		IResolutionContext resolver = new CachingResolutionContext(config);
		resolver.getRepositories().add(new WorkspaceRepository(resolver, new File("../../com.unnsvc.erhena/")));
		resolver.getRepositories().add(new WorkspaceRepository(resolver, new File("../../")));

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1");
		IRhenaModule model = resolver.materialiseModel(entryPointIdentifier);

		EExecutionType type = EExecutionType.PROTOTYPE;
		IRhenaEdge entryPointEdge = new RhenaEdge(type, model, TraverseType.SCOPE);
		GraphResolver graphResovler = new GraphResolver(resolver);
		graphResovler.resolveReferences(entryPointEdge);

		new ParallelGraphProcessor(resolver).processEdges(resolver.getEdges());

	}

}
