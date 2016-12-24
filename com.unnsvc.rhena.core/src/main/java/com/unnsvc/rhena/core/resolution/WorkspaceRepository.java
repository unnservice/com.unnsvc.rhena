
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.RhenaConstants;
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
import com.unnsvc.rhena.core.execution.RhenaExecution;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.visitors.DependencyCollector;

/**
 * @TODO cache lifecycle over multiple execution generation?
 * @author noname
 *
 */
public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		Map<EExecutionType, List<IRhenaExecution>> deps = new EnumMap<EExecutionType, List<IRhenaExecution>>(EExecutionType.class);
		for (EExecutionType et : EExecutionType.values()) {
			deps.put(et, new ArrayList<IRhenaExecution>());
		}

		// get dependency chains of dependencies
		getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

		// get the other executions of this module into the dependencies
		for (EExecutionType depEt : entryPoint.getExecutionType().getTraversables()) {

			IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(depEt);
			if (deps.get(depEt).contains(exec)) {
				deps.get(depEt).remove(exec);
			}
			deps.get(depEt).add(exec);
		}

		// debug dependency chains
		deps.forEach((key, val) -> val.forEach(exec -> config.getLogger(getClass()).debug(key + ": " + exec)));

		/**
		 * @TODO we only want a certain execution type for each execution
		 */
		IRhenaModule module = cache.getModule(entryPoint.getTarget());
		if (module.getLifecycleName() != null && module.getLifecycleName() != RhenaConstants.DEFAULT_LIFECYCLE_NAME) {

			return runInExecutableLifecycle(cache, entryPoint, module);
		} else {

			return runInDefaultExecutableLifecycle(cache, entryPoint, module);
		}
	}

	private IRhenaExecution runInDefaultExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		return new RhenaExecution(entryPoint.getTarget(), entryPoint.getExecutionType(),
				new ArtifactDescriptor(entryPoint.getTarget().toString(), "http://not.implemented", "not-implemented"));
	}

	/**
	 * @TODO perform injection
	 * @param cache
	 * @param entryPoint
	 * @param module
	 * @return
	 * @throws RhenaException
	 */
	private IRhenaExecution runInExecutableLifecycle(IRhenaCache cache, IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		ILifecycleReference lifecycleRef = module.getLifecycleDeclarations().get(module.getLifecycleName());

		IExecutionContext context = constructLifecycleProcessor(cache, lifecycleRef.getContext(), IExecutionContext.class, new Class[] { IRhenaCache.class },
				cache);
		context.configure(module, lifecycleRef.getContext().getConfiguration());

		for (IProcessorReference proc : lifecycleRef.getProcessors()) {

			IProcessor processor = constructLifecycleProcessor(cache, proc, IProcessor.class, new Class[] { IRhenaCache.class, IExecutionContext.class }, cache,
					context);
			processor.configure(module, proc.getConfiguration());
			// and execute it...
		}

		// and finally, execute the generator
		IGenerator generator = constructLifecycleProcessor(cache, lifecycleRef.getGenerator(), IGenerator.class,
				new Class[] { IRhenaCache.class, IExecutionContext.class }, cache, context);
		generator.configure(module, lifecycleRef.getGenerator().getConfiguration());
		// and execute it to produce final artifact...

		File generated = generator.generate(context, module, entryPoint.getExecutionType());
		
		/**
		 * @TODO validate filename so it conforms to the spec, so it can be used
		 *       as a dependency, or have a remote artifact descriptor which
		 *       describes the artifacts produces in each execution
		 */

		try {
			return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(), generated.getCanonicalFile().toURI().toURL(), generateSha1(generated)));
		} catch (IOException mue) {
			throw new RhenaException(mue.getMessage(), mue);
		}
	}

	private String generateSha1(File generated) throws RhenaException {

		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
			try (InputStream input = new FileInputStream(generated)) {
				byte[] buffer = new byte[8192];
				int len = input.read(buffer);

				while (len != -1) {
					digest.update(buffer, 0, len);
					len = input.read(buffer);
				}
				return new HexBinaryAdapter().marshal(digest.digest());
			}
		} catch (NoSuchAlgorithmException | IOException nsae) {
			throw new RhenaException(nsae.getMessage(), nsae);
		} finally {
			if (digest != null) {
				try {
					digest.clone();
				} catch (CloneNotSupportedException e) {

					throw new RhenaException(e.getMessage(), e);
				}
			}
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

		DependencyCollector coll = new DependencyCollector(cache, processor.getModuleEdge());
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

	private void getDepchain(Map<EExecutionType, List<IRhenaExecution>> deps, IRhenaCache cache, ModuleIdentifier identifier, EExecutionType et)
			throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);

		/**
		 * Collect dependency information
		 */
		for (IRhenaEdge edge : module.getDependencies()) {
			IRhenaModule depmod = cache.getModule(identifier);
			DependencyCollector coll = new DependencyCollector(cache, edge);
			depmod.visit(coll);

			for (IRhenaExecution exec : coll.getDependencies()) {
				if (deps.get(edge.getEntryPoint().getExecutionType()).contains(exec)) {
					deps.get(edge.getEntryPoint().getExecutionType()).remove(exec);
				}
				deps.get(edge.getEntryPoint().getExecutionType()).add(exec);
			}
		}
	}
}
