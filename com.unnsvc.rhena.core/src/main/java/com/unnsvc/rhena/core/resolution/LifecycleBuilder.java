
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.lifecycle.Lifecycle;
import com.unnsvc.rhena.core.visitors.Dependencies;
import com.unnsvc.rhena.core.visitors.DependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultContext;
import com.unnsvc.rhena.lifecycle.DefaultGenerator;
import com.unnsvc.rhena.lifecycle.DefaultJavaProcessor;
import com.unnsvc.rhena.lifecycle.DefaultManifestProcessor;

public class LifecycleBuilder {

	private IRhenaModule module;
	private IRhenaContext context;

	public LifecycleBuilder(IRhenaModule module, IRhenaContext context) {

		this.module = module;
		this.context = context;
	}

	public ILifecycle buildLifecycle(IEntryPoint entryPoint, String lifecycleName) throws RhenaException {

		ILifecycle lifecycle = context.getCache().getLifecycles().get(entryPoint.getTarget());
		if (lifecycle == null) {
			// construct lifecycle

			if (module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
				lifecycle = constructDefaultLifecycle(entryPoint);
			} else {
				lifecycle = constructCustomLifecycle(entryPoint);
			}

			context.getCache().getLifecycles().put(entryPoint.getTarget(), lifecycle);
		}
		return lifecycle;
	}

	private ILifecycle constructCustomLifecycle(IEntryPoint entryPoint) throws RhenaException {

		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());

		IExecutionContext contextProcessor = constructLifecycleProcessor(context.getCache(), lifecycleRef.getContext(), IExecutionContext.class,
				new Class[] { IRhenaCache.class }, context.getCache());
		contextProcessor.configure(module, lifecycleRef.getContext().getConfiguration());

		List<IProcessor> processors = new ArrayList<IProcessor>();
		for (IProcessorReference proc : lifecycleRef.getProcessors()) {

			IProcessor processor = constructLifecycleProcessor(context.getCache(), proc, IProcessor.class,
					new Class[] { IRhenaCache.class, IExecutionContext.class }, context.getCache(), contextProcessor);
			processor.configure(module, proc.getConfiguration());
			processors.add(processor);
		}

		// and finally, execute the generator
		IGenerator generator = constructLifecycleProcessor(context.getCache(), lifecycleRef.getGenerator(), IGenerator.class,
				new Class[] { IRhenaCache.class, IExecutionContext.class }, context.getCache(), contextProcessor);
		generator.configure(module, lifecycleRef.getGenerator().getConfiguration());

		ILifecycle lifecycle = new Lifecycle(contextProcessor, generator, processors);
		return lifecycle;
	}

	/**
	 * @TODO checks for constructor validity and type conformance, gc
	 *       classloaders properly
	 * @param cache
	 * @param processor
	 * @param type
	 * @return
	 * @throws RhenaException
	 */
	@SuppressWarnings("unchecked")
	private <T extends ILifecycleProcessor> T constructLifecycleProcessor(IRhenaCache cache, ILifecycleProcessorReference processor, Class<T> type,
			Class<?>[] argTypes, Object... args) throws RhenaException {

		DependencyCollectionVisitor coll = new DependencyCollectionVisitor(cache, processor.getModuleEdge());
		List<URL> deps = new ArrayList<URL>();
		for (IRhenaExecution exec : coll.getDependencies()) {
			deps.add(exec.getArtifact().getArtifactUrl());
		}

		/**
		 * @TODO Determine strategy for closing the classloader later in the
		 *       lifecycle?
		 */
		URLClassLoader urlc = new URLClassLoader(deps.toArray(new URL[deps.size()]), Thread.currentThread().getContextClassLoader());

		try {
			Class<T> clazz = (Class<T>) urlc.loadClass(processor.getClazz());
			Constructor<T> constr = clazz.getConstructor(argTypes);
			return constr.newInstance(args);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private ILifecycle constructDefaultLifecycle(IEntryPoint entryPoint) throws RhenaException {

		/**
		 * Build and configure lifecycle
		 */
		IExecutionContext contextProc = new DefaultContext(context.getCache());
		contextProc.configure(module, createDefaultContextConfiguration());

		IProcessor javaProcessor = new DefaultJavaProcessor(context.getCache(), contextProc);
		javaProcessor.configure(module, Utils.newEmptyDocument());
		IProcessor manifestProcessor = new DefaultManifestProcessor(context.getCache(), contextProc);
		manifestProcessor.configure(module, Utils.newEmptyDocument());

		List<IProcessor> processors = new ArrayList<IProcessor>();
		processors.add(javaProcessor);
		processors.add(manifestProcessor);

		IGenerator generator = new DefaultGenerator(context.getCache(), contextProc);
		generator.configure(module, Utils.newEmptyDocument());

		ILifecycle lifecycle = new Lifecycle(contextProc, generator, processors);

		return lifecycle;
	}

	private Document createDefaultContextConfiguration() throws RhenaException {

		Document doc = null;
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			fact.setNamespaceAware(true);
			DocumentBuilder db = fact.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(""
					+ "			<context module=\"com.unnsvc.rhena:lifecycle:0.0.1\" class=\"com.unnsvc.rhena.lifecycle.DefaultContext\">\n"
					+ "				<resources>\n" + "					<main path=\"src/main/java\" />\n"
					+ "					<main path=\"src/main/resources\" filter=\"true\" />\n" + "					<test path=\"src/test/java\" />\n"
					+ "					<test path=\"src/test/resources\" filter=\"true\" />\n" + "				</resources>\n" + "			</context>"));

			doc = db.parse(is);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
		return doc;
	}

}

// public void dummy() {
// /**
// * @TODO we only want a certain execution types for each execution
// */
// if (module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {
//
// return runInExecutableLifecycle(cache, entryPoint, module, deps);
// } else {
//
// return runInDefaultExecutableLifecycle(cache, entryPoint, module, deps);
// }
// }
