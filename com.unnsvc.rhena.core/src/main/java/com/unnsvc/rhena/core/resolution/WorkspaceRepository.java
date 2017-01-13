
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.agent.ILifecycleAgent;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.lifecycle.CustomProcessorExecutable;
import com.unnsvc.rhena.core.lifecycle.LifecycleExecutable;
import com.unnsvc.rhena.core.lifecycle.ProcessorExecutable;
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

	private static final long serialVersionUID = 1L;

	public WorkspaceRepository(IRhenaContext context, File location) {

		super(context, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		IRhenaModule module = cache.getModule(entryPoint.getTarget());

		if (entryPoint.getExecutionType().equals(EExecutionType.MODEL)) {

			File workspaceDirectory = new File(module.getLocation().getPath());
			File moduleDescriptor = new File(workspaceDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				String sha1sum = Utils.generateSha1(moduleDescriptor);
				IArtifactDescriptor descriptor = new ArtifactDescriptor(entryPoint.getTarget().toString(), moduleDescriptor.getCanonicalFile().toURI().toURL(), sha1sum);
				return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), descriptor);
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

			Dependencies deps = new Dependencies(entryPoint.getExecutionType());

			// get dependency chains of dependencies
			getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

			/**
			 * Up to, but not with, the ordinal, becauuse that's the one we
			 * willimgur create next by executing a lifecycle
			 */
			for (int i = 0; i < entryPoint.getExecutionType().ordinal(); i++) {

				IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(EExecutionType.values()[i]);
				deps.addDependency(EExecutionType.values()[i], exec);
			}

			try {

				/**
				 * New method of constructing the lifecycle attempts to produce
				 * a lifecycle in an intermediate format which will make it easy
				 * for the agent to construct and execute lifecycle
				 */
				LifecycleExecutable lifecycleExecutable = new LifecycleExecutable(RhenaConstants.DEFAULT_LIFECYCLE_NAME);
				if (module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
					lifecycleExecutable.setContextExecutable(new ProcessorExecutable(DefaultContext.class.getName()));
					lifecycleExecutable.addProcessorExecutable(new ProcessorExecutable(DefaultJavaProcessor.class.getName()));
					lifecycleExecutable.addProcessorExecutable(new ProcessorExecutable(DefaultManifestProcessor.class.getName()));
					lifecycleExecutable.setGenerator(new ProcessorExecutable(DefaultGenerator.class.getName()));
				} else {
					ILifecycleReference lifecycleReference = module.getLifecycleDeclarations().get(module.getLifecycleName());
					ILifecycleProcessorReference context = lifecycleReference.getContext();
					lifecycleExecutable.setContextExecutable(new CustomProcessorExecutable(context, getDependencies(context.getModuleEdge())));
					for (ILifecycleProcessorReference processor : lifecycleReference.getProcessors()) {
						lifecycleExecutable.addProcessorExecutable(new CustomProcessorExecutable(processor, getDependencies(processor.getModuleEdge())));
					}
					ILifecycleProcessorReference generator = lifecycleReference.getGenerator();
					lifecycleExecutable.setGenerator(new CustomProcessorExecutable(generator, getDependencies(generator.getModuleEdge())));
				}

				ILifecycleAgent agent = context.getLifecycleAgentManager().getLifecycleAgent();
				ILifecycleExecutionResult generated = agent.executeLifecycle(lifecycleExecutable, module, entryPoint.getExecutionType(), deps);

				/**
				 * Old method below
				 */

				File generatedFile = generated.getGeneratedArtifact().getCanonicalFile();
				String sha1sum = Utils.generateSha1(generatedFile);
				IArtifactDescriptor descriptor = new ArtifactDescriptor(entryPoint.getTarget().toString(), generatedFile.toURI().toURL(), sha1sum);
				return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), descriptor, generated.getInputs());
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		}
	}

	private List<URL> getDependencies(IRhenaEdge moduleEdge) throws RhenaException {

		DependencyCollectionVisitor coll = new DependencyCollectionVisitor(context.getCache(), moduleEdge);
		List<URL> deps = new ArrayList<URL>();
		for (IRhenaExecution exec : coll.getDependencies()) {
			deps.add(exec.getArtifact().getArtifactUrl());
		}
		return coll.getDependenciesURL();
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
