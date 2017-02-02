
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.agent.ILifecycleAgent;
import com.unnsvc.rhena.common.agent.ILifecycleExecutionResult;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.ExplodedArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.search.IDependencies;
import com.unnsvc.rhena.common.search.URLDependencyTreeVisitor;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.lifecycle.CommandProcessorReference;
import com.unnsvc.rhena.core.lifecycle.CustomCommandExecutable;
import com.unnsvc.rhena.core.lifecycle.CustomProcessorExecutable;
import com.unnsvc.rhena.core.lifecycle.LifecycleExecutable;
import com.unnsvc.rhena.core.lifecycle.ProcessorExecutable;
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

	public WorkspaceRepository(IRhenaContext context, File location) {

		super(context, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, ICaller caller) throws RhenaException {

		IRhenaModule module = caller.getModule();

		if (caller.getExecutionType().equals(EExecutionType.MODEL)) {

			File workspaceDirectory = new File(module.getLocation().getPath());
			File moduleDescriptor = new File(workspaceDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				IArtifactDescriptor descriptor = new ExplodedArtifactDescriptor(IArtifactDescriptor.DEFAULT, caller.getIdentifier().toString(), moduleDescriptor.getCanonicalFile().toURI().toURL());
				return new WorkspaceExecution(caller.getIdentifier(), caller.getExecutionType(), Collections.singletonList(descriptor));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

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
					ILifecycleReference lifecycleReference = module.getMergedLifecycleDeclarations(cache).get(module.getLifecycleName());
					ILifecycleProcessorReference context = lifecycleReference.getContext();
					lifecycleExecutable.setContextExecutable(new CustomProcessorExecutable(context, getLifecycleDependencies(context.getModuleEdge())));
					for (ILifecycleProcessorReference processor : lifecycleReference.getProcessors()) {
						lifecycleExecutable
								.addProcessorExecutable(new CustomProcessorExecutable(processor, getLifecycleDependencies(processor.getModuleEdge())));
					}

					ILifecycleProcessorReference generator = lifecycleReference.getGenerator();
					lifecycleExecutable.setGenerator(new CustomProcessorExecutable(generator, getLifecycleDependencies(generator.getModuleEdge())));

					for (ILifecycleProcessorReference command : lifecycleReference.getCommands()) {
						CommandProcessorReference commandRef = (CommandProcessorReference) command;
						lifecycleExecutable.addCommandExecutable(new CustomCommandExecutable(commandRef, getLifecycleDependencies(commandRef.getModuleEdge())));
					}
				}

				ILifecycleAgent agent = context.getLifecycleAgentManager().getLifecycleAgent();
				ILifecycleExecutionResult generated = agent.executeLifecycle(getContext().getCache(), getContext().getConfig(), caller, lifecycleExecutable);

				List<IArtifactDescriptor> descriptors = generated.getGeneratedArtifacts();
//				List<IArtifactDescriptor> descriptors = new ArrayList<IArtifactDescriptor>();
//				for (IResult generatedFile : results) {
//					if (generatedFile instanceof IExplodedResult) {
//						IExplodedResult explodedResult = (IExplodedResult) generatedFile;
//						IArtifactDescriptor descriptor = new ExplodedArtifactDescriptor(explodedResult.getName(), explodedResult.getResultUrl(), explodedResult.getSourceUrl());
//						descriptors.add(descriptor);
//					} else {
//						String sha1sum = Utils.generateSha1(new File(generatedFile.getResultUrl().getFile()));
//						IArtifactDescriptor descriptor = new PackagedArtifactDescriptor(generatedFile.getName(), generatedFile.getResultUrl(), sha1sum);
//						descriptors.add(descriptor);
//					}
//				}

				return new WorkspaceExecution(caller.getIdentifier(), caller.getExecutionType(), descriptors, generated.getInputs());
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		}
	}

	private IDependencies getLifecycleDependencies(IRhenaEdge moduleEdge) throws RhenaException {

		IRhenaCache cache = context.getCache();
		IRhenaModule module = cache.getModule(moduleEdge.getEntryPoint().getTarget());
		URLDependencyTreeVisitor coll = new URLDependencyTreeVisitor(cache, moduleEdge.getEntryPoint().getExecutionType(), moduleEdge.getTraverseType());
		module.visit(coll);
		// include the actual lifecycle dependency too
		IRhenaExecution selfExecution = cache.getExecution(moduleEdge.getEntryPoint().getTarget()).get(moduleEdge.getEntryPoint().getExecutionType());
		coll.getExecutions(moduleEdge.getEntryPoint().getExecutionType()).add(0, selfExecution);

		return coll.getDependencies();
	}
}
