
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

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
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.visitors.Dependencies;
import com.unnsvc.rhena.core.visitors.DependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultContext;
import com.unnsvc.rhena.lifecycle.DefaultGenerator;
import com.unnsvc.rhena.lifecycle.DefaultProcessor;

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

		Dependencies deps = new Dependencies(entryPoint.getExecutionType());
		
		// get dependency chains of dependencies
		getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

		// get the other executions of this module into the dependencies
		for (EExecutionType depEt : entryPoint.getExecutionType().getTraversables()) {

			IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(depEt);

			deps.addDependency(depEt, exec);
		}

		// debug dependency chains
		deps.getDependencies().forEach((key, val) -> val.forEach(exec -> config.getLogger(getClass()).debug(key + ": " + exec)));

		/**
		 * @TODO we only want a certain execution type for each execution
		 */
		IRhenaModule module = cache.getModule(entryPoint.getTarget());
		if (module.getLifecycleName() != null && module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {

			return runInExecutableLifecycle(cache, entryPoint, module, deps);
		} else {

			return runInDefaultExecutableLifecycle(cache, entryPoint, module, deps);
		}
	}

	private IRhenaExecution runInDefaultExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module, Dependencies deps) throws RhenaException {

		IExecutionContext context = new DefaultContext(cache);
		context.configure(module, Utils.newEmptyDocument());

		IProcessor processor = new DefaultProcessor(cache, context);
		processor.configure(module, Utils.newEmptyDocument());
		processor.process(context, module, entryPoint.getExecutionType(), deps);

		IGenerator generator = new DefaultGenerator(cache, context);
		generator.configure(module, Utils.newEmptyDocument());

		File generated = generator.generate(context, module, entryPoint.getExecutionType());

		try {
			return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(),
					new ArtifactDescriptor(entryPoint.getTarget().toString(), generated.getCanonicalFile().toURI().toURL(), Utils.generateSha1(generated)));
		} catch (IOException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
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
	private IRhenaExecution runInExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module, Dependencies deps) throws RhenaException {

		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());

		IExecutionContext context = constructLifecycleProcessor(cache, lifecycleRef.getContext(), IExecutionContext.class, new Class[] { IRhenaCache.class }, cache);
		context.configure(module, lifecycleRef.getContext().getConfiguration());

		for (IProcessorReference proc : lifecycleRef.getProcessors()) {

			IProcessor processor = constructLifecycleProcessor(cache, proc, IProcessor.class, new Class[] { IRhenaCache.class, IExecutionContext.class }, cache, context);
			processor.configure(module, proc.getConfiguration());
			// and execute it...
			processor.process(context, module, entryPoint.getExecutionType(), deps);
		}

		// and finally, execute the generator
		IGenerator generator = constructLifecycleProcessor(cache, lifecycleRef.getGenerator(), IGenerator.class, new Class[] { IRhenaCache.class, IExecutionContext.class }, cache, context);
		generator.configure(module, lifecycleRef.getGenerator().getConfiguration());
		// and execute it to produce final artifact...

		File generated = generator.generate(context, module, entryPoint.getExecutionType());

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
		 * @TODO Resource leak, refactor code to close the classloader
		 *       eventually...
		 */
		URLClassLoader urlc = new URLClassLoader(deps.toArray(new URL[deps.size()]));

		try {
			Class<T> clazz = (Class<T>) urlc.loadClass(processor.getClazz());
			Constructor<T> constr = clazz.getConstructor(argTypes);
			return constr.newInstance(args);
		} catch (Exception ex) {
			throw new RhenaException(ex.getMessage(), ex);
		}
	}

	private void getDepchain(Dependencies deps, IRhenaCache cache, ModuleIdentifier identifier, EExecutionType et)
			throws RhenaException {

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
