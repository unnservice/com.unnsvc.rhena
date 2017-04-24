
package com.unnsvc.rhena.execution.builders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.DependencyCollector;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.execution.requests.ExecutionRequest;

/**
 * Builders are executed inside of separate threads
 * 
 * @author noname
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
			
			ExecutionRequest request = new ExecutionRequest(entryPoint, module, dependencies);
			IExecutionResult result = client.executeRequest(request);
			return result;
		}
	}

}
