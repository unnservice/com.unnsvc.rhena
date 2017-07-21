
package com.unnsvc.rhena.execution.builders;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.config.IAgentConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.lifecycle.ICommandInstance;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IProcessorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.DependencyCollector;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.execution.requests.ExecutionRequest;
import com.unnsvc.rhena.lifecycle.execution.DefaultLifecycleFactory;
import com.unnsvc.rhena.lifecycle.execution.LifecycleInstance;
import com.unnsvc.rhena.lifecycle.instance.ContextProcessorInstance;
import com.unnsvc.rhena.lifecycle.instance.GeneratorProcessorInstance;
import com.unnsvc.rhena.lifecycle.instance.ProcessorInstance;

/**
 * Builders are executed inside of separate threads
 * 
 * @author Paul Alesius
 *
 */
public class WorkspaceBuilder extends AbstractBuilder {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaContext context;
	private IEntryPoint entryPoint;
	private IRhenaModule module;

	public WorkspaceBuilder(IRhenaContext context, IEntryPoint entryPoint, IRhenaModule module) {

		this.context = context;
		this.entryPoint = entryPoint;
		this.module = module;
	}

	@Override
	public IExecutionResponse call() throws Exception {

		debugBuilderRun(module, context, entryPoint);

		IAgentConfiguration agentConfig = context.getConfig().getAgentConfiguration();
		IRhenaAgentClient client = context.getFactories().getAgentClientFactory().newClient(agentConfig.getAgentAddress());

		DependencyCollector collector = new DependencyCollector(context, entryPoint);
		IDependencies dependencies = collector.toDependencyChain();

		ILifecycleInstance lifecycle = instantiateLifecycle(module.getLifecycleSpecification());

		ExecutionRequest request = new ExecutionRequest(entryPoint, module, lifecycle, dependencies);
		log.debug(request.toString());
		IExecutionResponse result = (IExecutionResponse) client.submitRequest(request);

		return result;
	}

	private ILifecycleInstance instantiateLifecycle(ILifecycleSpecification lifecycle) throws RhenaException {

		log.trace("Lifecycle is: " + lifecycle);

		ILifecycleInstance instance = null;

		if (lifecycle.getName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {

			instance = DefaultLifecycleFactory.createDefaultLifecycle();
		} else {
			/**
			 * Processors
			 */
			IDependencies contextDepchain = createDepchain(lifecycle.getContextReference());
			ContextProcessorInstance context = new ContextProcessorInstance(contextDepchain, lifecycle.getContextReference().getClazz());

			List<IProcessorInstance> processors = new ArrayList<IProcessorInstance>();
			for (IProcessorSpec procref : lifecycle.getProcessorReferences()) {
				IDependencies processorDepchain = createDepchain(procref);
				ProcessorInstance processor = new ProcessorInstance(processorDepchain, procref.getClazz());
				processors.add(processor);
			}

			IDependencies generatorDepchain = createDepchain(lifecycle.getGeneratorReference());
			GeneratorProcessorInstance generator = new GeneratorProcessorInstance(generatorDepchain, lifecycle.getGeneratorReference().getClazz());

			List<ICommandInstance> commands = new ArrayList<ICommandInstance>();

			// for (ICommandSpec cmdref : lifecycle.getCommandReferences()) {
			//
			// IDependencies commandDepchain = createDepchain(cmdref);
			// CommandProcessorInstance command = new
			// CommandProcessorInstance(commandDepchain, cmdref.getClazz());
			// commands.add(command);
			// }

			/**
			 * Lifecycle instance
			 */
			IDependencies dependencies = createDepchain(lifecycle);

			instance = new LifecycleInstance(lifecycle.getName(), dependencies, context, processors, generator, commands);
		}

		return instance;
	}

	private IDependencies createDepchain(Iterable<IRhenaEdge> dependencyContainer) throws RhenaException {

		IDependencies contextDependencies = null;
		for (IRhenaEdge contextDep : dependencyContainer) {
			IDependencies depchain = new DependencyCollector(context, contextDep.getEntryPoint()).toDependencyChain();

			if (contextDependencies == null) {

				contextDependencies = depchain;
			} else {

				contextDependencies.merge(depchain);
			}
		}
		return contextDependencies;
	}
}
