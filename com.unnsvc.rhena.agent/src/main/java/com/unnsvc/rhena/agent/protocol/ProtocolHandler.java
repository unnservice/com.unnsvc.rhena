
package com.unnsvc.rhena.agent.protocol;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.agent.messages.ExecutionResponse;
import com.unnsvc.rhena.common.IRhenaAgentServer;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.lifecycle.IContext;
import com.unnsvc.rhena.common.lifecycle.IGenerator;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.lifecycle.IProcessor;
import com.unnsvc.rhena.common.lifecycle.IProcessorInstance;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.objectserver.handler.IProtocolHandler;
import com.unnsvc.rhena.objectserver.messages.ExceptionResponse;
import com.unnsvc.rhena.objectserver.messages.IRequest;
import com.unnsvc.rhena.objectserver.messages.IResponse;
import com.unnsvc.rhena.objectserver.messages.PingRequest;
import com.unnsvc.rhena.objectserver.messages.PingResponse;

public class ProtocolHandler implements IProtocolHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaAgentServer agentServer;

	public ProtocolHandler(IRhenaAgentServer agentServer) {

		this.agentServer = agentServer;
	}

	@Override
	public IResponse handleRequest(IRequest request) {

		/**
		 * Determine request type
		 */
		try {

			if (request instanceof IExecutionRequest) {

				return handleExecutionRequest((IExecutionRequest) request);
			} else if (request instanceof PingRequest) {

				return new PingResponse(((PingRequest) request).getId());
			} else {

				throw new RhenaException("Unknown request type: " + request.getClass().getName());
			}
		} catch (RhenaException ose) {

			return new ExceptionResponse(ose);
		}
	}

	public IExecutionResponse handleExecutionRequest(IExecutionRequest request) throws RhenaException {

		ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();

		ILifecycleInstance lifecycle = request.getLifecycle();
		URLClassLoader lifecycleClassLoader = createClassLoader(lifecycle.getDependencies(), parentClassLoader);

		IContext context = instantiate(lifecycleClassLoader, lifecycle.getContext(), IContext.class);

		for (IProcessorInstance processorInst : lifecycle.getProcessors()) {
			IProcessor processor = instantiate(lifecycleClassLoader, processorInst, IProcessor.class);
			// ...
		}

		IGenerator generator = instantiate(lifecycleClassLoader, lifecycle.getGenerator(), IGenerator.class);

		/**
		 * @TODO command
		 */
		// if(lifecycle.getCommands() != null) {
		//
		// ICommand command = instantiate(lifecycleClassLoader,
		// lifecycle.getCommands(), ICommand.class);
		//
		// }

		return new ExecutionResponse(request.getEntryPoint(), request.getModule());
	}

	@SuppressWarnings("unchecked")
	private <T extends IProcessor> T instantiate(URLClassLoader lifecycleClassLoader, IProcessorInstance processorInst, Class<T> type) throws RhenaException {

		URLClassLoader processorClassLoader = createClassLoader(processorInst.getDependencies(), lifecycleClassLoader);
		try {

			Class<T> processorType = (Class<T>) processorClassLoader.loadClass(processorInst.getClassName());
			T processor = processorType.newInstance();
			return processor;
		} catch (Throwable t) {
			throw new RhenaException(t);
		}
	}

	/**
	 * @TODO optimize if possible, as this is called many times
	 * @param executionType
	 * @param dependencies
	 * @param contextClassLoader
	 * @return
	 */
	private URLClassLoader createClassLoader(IDependencies dependencies, ClassLoader parentClassLoader) {

		EExecutionType executionType = EExecutionType.MAIN;

		List<URL> depchain = new ArrayList<URL>();
		for (IExecutionResponse result : dependencies) {

			if (result.getEntryPoint().getExecutionType().lessOrEqualTo(executionType)) {
				result.getArtifacts().forEach(artifact -> {

					for (String tag : artifact.getTags()) {
						if (tag.equals(RhenaConstants.DEFAULT_ARTIFACT_TAG)) {
							depchain.add(artifact.getLocation());
						}
					}
				});
			}
		}

		return new URLClassLoader(depchain.toArray(new URL[] {}), parentClassLoader);
	}
}
