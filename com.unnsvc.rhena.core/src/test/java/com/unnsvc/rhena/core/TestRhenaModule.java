
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.model.processing.GraphResolver;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;

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

		RhenaConfiguration config = new RhenaConfiguration();
		IResolutionContext context = new CachingResolutionContext(config);
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../com.unnsvc.erhena/")));
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../")));

		ModuleIdentifier entryPointIdentifier = ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1");
		IRhenaModule model = context.materialiseModel(entryPointIdentifier);

		GraphResolver gr = new GraphResolver(context);

		EExecutionType type = EExecutionType.PROTOTYPE;
		gr.resolveReferences(new RhenaEdge(type, model, TraverseType.SCOPE));

		
		
		/**
		 * Test debug log
		 */
//		for (ILifecycleDeclaration lifecycle : gr.getLifecycles()) {
//			log.info("Lifecycle " + lifecycle.getName());
//			lifecycle.getContext().getModuleEdge().getTarget().visit(new LoggingVisitor(lifecycle.getContext().getModuleEdge().getExecutionType(), context));
//			lifecycle.getProcessors().forEach(processor -> {
//				try {
//					processor.getModuleEdge().getTarget().visit(new LoggingVisitor(processor.getModuleEdge().getExecutionType(), context));
//				} catch (RhenaException e) {
//
//					e.printStackTrace();
//				}
//			});
//			lifecycle.getGenerator().getModuleEdge().getTarget().visit(new LoggingVisitor(lifecycle.getGenerator().getModuleEdge().getExecutionType(), context));
//		}
//		log.info("Execution plan: ");
//		model.visit(new LoggingVisitor(type, context));

		// model.visit(new EventedVisitor(EnterType.AFTER, new
		// ModelInitialisingHandler(context)).setEnterUnusedLifecycle(true));
		// context.materialiseExecution(model, ExecutionType.DELIVERABLE);

		// model.visit(new ModelInitialisingVisitor(context));
		// model.visit(new ModelMergeVisitor(context));
		// model.visit(new ModelBuildingVisitor(context));

		log.info("Finished");
	}

}
