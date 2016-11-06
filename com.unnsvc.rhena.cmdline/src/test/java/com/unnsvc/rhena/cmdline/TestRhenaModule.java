
package com.unnsvc.rhena.cmdline;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.core.RhenaConfiguration;
import com.unnsvc.rhena.core.execution.GraphResolver;
import com.unnsvc.rhena.core.execution.ParallelGraphProcessor;
import com.unnsvc.rhena.core.logging.LogEvent;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;

public class TestRhenaModule {

	@Test
	public void test() throws Exception {

		long start = System.currentTimeMillis();
		execute();
		long end = System.currentTimeMillis();

		/**
		 * @TODO This is where metrics come into play, implement metrics
		 */
		// log.info("Executed in " + (end - start) + "ms (core: Xms, lifecycles:
		// Xms)");
	}

	/**
	 * @TODO Need some sort of execution context which does the saving and
	 *       caching during one execution, so every execution ends up with an
	 *       entirely new model, then drop model
	 * 
	 * @throws Exception
	 */
	private void execute() throws Exception {

		RhenaConfiguration config = new RhenaConfiguration();

		IRhenaContext context = new CachingResolutionContext(config);
		context.addListener(new IContextListener<LogEvent>() {

			@Override
			public void onEvent(LogEvent event) throws RhenaException {

				Logger log = LoggerFactory.getLogger(event.getSource());

				String message = "[" + (event.getIdentifier() == null ? "core" : event.getIdentifier()) + "] " + event.getMessage();

				switch (event.getLevel()) {
					case TRACE:
						log.trace(message);
						break;
					case DEBUG:
						log.debug(message);
						break;
					case INFO:
						log.info(message);
						break;
					case WARN:
						log.warn(message);
						break;
					case ERROR:
						log.error(message);
						break;
				}
			}

			@Override
			public Class<LogEvent> getType() {

				return LogEvent.class;
			}
		});

		// context.addListener(new MetricsListenr());

		context.getRepositories().add(new WorkspaceRepository(context, new File("../../com.unnsvc.erhena/")));
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../")));

		// IRhenaModule model =
		// context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1"));
		IRhenaModule model = context.materialiseWorkspaceModel("com.unnsvc.erhena.core");

		IRhenaEdge entryPointEdge = new RhenaEdge(EExecutionType.PROTOTYPE, model.getModuleIdentifier(), TraverseType.SCOPE);

		GraphResolver graphResovler = new GraphResolver(context);
		graphResovler.resolveReferences(entryPointEdge);

		debugModel(context, EExecutionType.PROTOTYPE, model);

		new ParallelGraphProcessor(context).processEdges(context.getEdges());

	}

	private void debugModel(IRhenaContext context, EExecutionType type, IRhenaModule model) throws Exception {

		ILifecycleDeclaration decl = model.getLifecycleDeclaration(model.getLifecycleName());

		context.materialiseModel(decl.getGenerator().getModuleEdge().getTarget()).visit(new LoggingVisitor(EExecutionType.FRAMEWORK, context));

		model.visit(new LoggingVisitor(type, context));
	}

}

// LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
// StatusManager statusManager = lc.getStatusManager();
//
// if (statusManager != null) {
// statusManager.add(new InfoStatus("Configuring logger", lc));
// }
//
// SiftingAppender sa = new SiftingAppender();
// sa.setName("SIFT");
// sa.setContext(lc);
//
// MDCBasedDiscriminator discriminator = new MDCBasedDiscriminator();
// discriminator.setKey("vhost");
// discriminator.setDefaultValue("administration");
// discriminator.start();
//
// sa.setDiscriminator(discriminator);
// see https://gist.github.com/kazimsarikaya/8645769