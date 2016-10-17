
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
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.execution.GraphResolver;
import com.unnsvc.rhena.core.execution.ParallelGraphProcessor;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;
import com.unnsvc.rhena.core.visitors.LoggingVisitor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

public class TestRhenaModule {

	// private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void test() throws Exception {

		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.detachAndStopAllAppenders();
		rootLogger.setLevel(Level.INFO);

		ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.unnsvc.rhena");
		log.setLevel(Level.INFO);

		LoggerContext c = rootLogger.getLoggerContext();

		ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setPattern("%d{ss.SSS} %-5level %40.40logger - %msg%n");
		encoder.setContext(c);
		encoder.start();
		ca.setEncoder(encoder);
		ca.setContext(c);
		ca.start();

		rootLogger.addAppender(ca);

		long start = System.currentTimeMillis();
		execute();
		long end = System.currentTimeMillis();

		/**
		 * @TODO This is where metrics come into play, implement metrics
		 */
		// log.info("Executed in " + (end - start) + "ms (core: Xms, lifecycles:
		// Xms)");
	}

	private void execute() throws Exception {

		RhenaConfiguration config = new RhenaConfiguration();
//		config.configureLoggingAppender();
		
		IRhenaContext context = new CachingResolutionContext(config);

		// context.addListener(new LogbackListener());
		// context.addListener(new MetricsListenr());

		context.getRepositories().add(new WorkspaceRepository(context, new File("../../com.unnsvc.erhena/")));
		context.getRepositories().add(new WorkspaceRepository(context, new File("../../")));

		IRhenaModule model = context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.erhena:core:0.0.1"));

		EExecutionType type = EExecutionType.PROTOTYPE;
		IRhenaEdge entryPointEdge = new RhenaEdge(type, model, TraverseType.SCOPE);

		GraphResolver graphResovler = new GraphResolver(context);
		graphResovler.resolveReferences(entryPointEdge);

		debugModel(context, type, model);

		new ParallelGraphProcessor(context).processEdges(context.getEdges());

	}

	private void debugModel(IRhenaContext context, EExecutionType type, IRhenaModule model) throws Exception {

		ILifecycleDeclaration decl = model.getLifecycleDeclaration(model.getLifecycleName());

		decl.getGenerator().getModuleEdge().getTarget().visit(new LoggingVisitor(EExecutionType.FRAMEWORK, context));

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