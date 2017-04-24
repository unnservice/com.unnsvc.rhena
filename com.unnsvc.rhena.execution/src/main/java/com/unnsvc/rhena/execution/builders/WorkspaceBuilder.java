
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.DependencyCollector;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.execution.requests.ExecutionRequest;
import com.unnsvc.rhena.lifecycle.execution.LifecycleExecution;

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
	public IExecutionResult call() throws Exception {

		debugBuilderRun(module, context, entryPoint);

		try (IRhenaAgentClient client = context.getFactories().getAgentClientFactory().newClient(context)) {

			DependencyCollector collector = new DependencyCollector(context, entryPoint);
			IDependencies dependencies = collector.toDependencyChain();

			ILifecycleInstance lifecycle = instantiateLifecycle(module.getLifecycleConfiguration());

			ExecutionRequest request = new ExecutionRequest(entryPoint, module, lifecycle, dependencies);
			IExecutionResult result = client.executeRequest(request);
			return result;
		}
	}

	private ILifecycleInstance instantiateLifecycle(ILifecycleConfiguration lifecycle) throws RhenaException {

		log.info("Lifecycle is: " + lifecycle);

		ILifecycleInstance instance = null;
		if (module.getLifecycleConfiguration().getName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {

			instance = new LifecycleExecution();
		} else {

			instance = new LifecycleExecution();
		}

		IDependencies dependencies = null;
		for (IRhenaEdge lifecycleEdge : lifecycle) {

			DependencyCollector collector = new DependencyCollector(context, lifecycleEdge.getEntryPoint());
			IDependencies lifecycleDeps = collector.toDependencyChain();
			if (dependencies == null) {

				dependencies = lifecycleDeps;
			} else {

				dependencies.merge(lifecycleDeps);
			}
		}

		return instance;
	}

}
