
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
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
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

/**
 * @TODO cache lifecycle over multiple executions?
 * @TODO MODEL gets generated here in the workspace repository?
 * @author noname
 *
 */
public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		IRhenaModule module = cache.getModule(entryPoint.getTarget());

		if (entryPoint.getExecutionType().equals(EExecutionType.MODEL)) {

			File workspaceDirectory = new File(module.getLocation().getPath());
			File moduleDescriptor = new File(workspaceDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(),
						moduleDescriptor.getCanonicalFile().toURI().toURL(), Utils.generateSha1(moduleDescriptor)));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

			Dependencies deps = new Dependencies(entryPoint.getExecutionType());

			// get dependency chains of dependencies
			getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

			/**
			 * Up to, but not with, the ordinal, becauuse that's the one we will
			 * create next with a lifecycle
			 */
			for (int i = 0; i < entryPoint.getExecutionType().ordinal(); i++) {

				IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(EExecutionType.values()[i]);
				deps.addDependency(EExecutionType.values()[i], exec);
			}

			/**
			 * @TODO we only want a certain execution type for each execution
			 */
			if (module.getLifecycleName() != null && module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {

				return runInExecutableLifecycle(cache, entryPoint, module, deps);
			} else {

				return runInDefaultExecutableLifecycle(cache, entryPoint, module, deps);
			}
		}
	}

	private IRhenaExecution runInDefaultExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module, Dependencies deps)
			throws RhenaException {

		ILifecycle lifecycle = cache.getLifecycles().get(entryPoint.getTarget());
		/**
		 * Build and configure lifecycle
		 */
		if(lifecycle == null) {
			IExecutionContext context = new DefaultContext(cache);
			context.configure(module, createDefaultContextConfiguration());
			
			IProcessor processor = new DefaultJavaProcessor(cache, context);
			processor.configure(module, Utils.newEmptyDocument());
			IProcessor manifestProcessor = new DefaultManifestProcessor(cache, context);
			manifestProcessor.configure(module, Utils.newEmptyDocument());
			
			List<IProcessor> processors = new ArrayList<IProcessor>();
			processors.add(processor);
			processors.add(manifestProcessor);
			
			IGenerator generator = new DefaultGenerator(cache, context);
			generator.configure(module, Utils.newEmptyDocument());

			lifecycle = new Lifecycle(context, generator, processors);
			cache.getLifecycles().put(entryPoint.getTarget(), lifecycle);
		}

		
		File generated = lifecycle.executeLifecycle(module, entryPoint.getExecutionType(), deps);

		try {
			return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(),
					new ArtifactDescriptor(entryPoint.getTarget().toString(), generated.getCanonicalFile().toURI().toURL(), Utils.generateSha1(generated)));
		} catch (IOException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
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

	/**
	 * @TODO perform injection
	 * @param cache
	 * @param entryPoint
	 * @param module
	 * @param deps
	 * @return
	 * @throws RhenaException
	 */
	private IRhenaExecution runInExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module, Dependencies dependencies) throws RhenaException {

		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());

		ILifecycle lifecycle = cache.getLifecycles().get(entryPoint.getTarget());
		if(lifecycle == null) {
			IExecutionContext context = constructLifecycleProcessor(cache, lifecycleRef.getContext(), IExecutionContext.class, new Class[] { IRhenaCache.class }, cache);
			context.configure(module, lifecycleRef.getContext().getConfiguration());
			
			List<IProcessor> processors = new ArrayList<IProcessor>();
			for (IProcessorReference proc : lifecycleRef.getProcessors()) {

				IProcessor processor = constructLifecycleProcessor(cache, proc, IProcessor.class, new Class[] { IRhenaCache.class, IExecutionContext.class }, cache, context);
				processor.configure(module, proc.getConfiguration());
				processors.add(processor);
			}
			
			// and finally, execute the generator
			IGenerator generator = constructLifecycleProcessor(cache, lifecycleRef.getGenerator(), IGenerator.class, new Class[] { IRhenaCache.class, IExecutionContext.class }, cache, context);
			generator.configure(module, lifecycleRef.getGenerator().getConfiguration());
			
			lifecycle = new Lifecycle(context, generator, processors);
			cache.getLifecycles().put(entryPoint.getTarget(), lifecycle);
		}

		// and execute it to produce final artifact...

		File generated = lifecycle.executeLifecycle(module, entryPoint.getExecutionType(), dependencies);

		/**
		 * @TODO validate filename so it conforms to the spec, so it can be used
		 *       as a dependency, or have a remote artifact descriptor which
		 *       describes the artifacts produces in each execution
		 */

		try {
			return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(),
					new ArtifactDescriptor(entryPoint.getTarget().toString(), generated.getCanonicalFile().toURI().toURL(), Utils.generateSha1(generated)));
		} catch (IOException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
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
		 * @TODO Determine strategy for closing the classloader later in the lifecycle?
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

	private void getDepchain(Dependencies deps, IRhenaCache cache, ModuleIdentifier identifier, EExecutionType et) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);

		/**
		 * Collect dependency information
		 */
		for (IRhenaEdge edge : module.getDependencies()) {
			IRhenaModule depmod = cache.getModule(identifier);
			DependencyCollectionVisitor coll = new DependencyCollectionVisitor(cache, edge);
			depmod.visit(coll);

			for (IRhenaExecution exec : coll.getDependencies()) {

				deps.addDependency(edge.getEntryPoint().getExecutionType(), exec);
			}
		}
	}
}
