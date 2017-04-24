
package com.unnsvc.rhena.agent;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.execution.IExecutionRequest;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.lifecycle.ILifecycleInstance;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.traversal.IDependencies;
import com.unnsvc.rhena.objectserver.IObjectServerAcceptor;

public class RhenaAgentAcceptor implements IObjectServerAcceptor<IExecutionRequest, IExecutionResult> {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public IExecutionResult onRequest(IExecutionRequest request) {

		log.debug("Executing in agent, returning result for " + request.getEntryPoint());

		return build(request);
	}

	/**
	 * On accept, we will build a classloader for the lifecycle, with the agent
	 * classloader as the parent
	 * 
	 * @param request
	 * @return
	 */
	protected ExecutionResult build(IExecutionRequest request) {

		EExecutionType executionType = EExecutionType.MAIN;
		ILifecycleInstance lifecycle = request.getLifecycle();
		URLClassLoader lifecycleClassLoader = createClassLoader(executionType, lifecycle.getDependencies(), Thread.currentThread().getContextClassLoader());

		return new ExecutionResult(request.getEntryPoint(), request.getModule());
	}

	private URLClassLoader createClassLoader(EExecutionType executionType, IDependencies dependencies, ClassLoader contextClassLoader) {

		List<URL> depchain = new ArrayList<URL>();
		for (IExecutionResult result : dependencies) {

			if (result.getEntryPoint().getExecutionType().lessOrEqualTo(executionType)) {
				result.getArtifacts().forEach(artifact -> {

					if (artifact.getTags().equals(RhenaConstants.DEFAULT_ARTIFACT_TAG)) {
						depchain.add(artifact.getLocation());
					}
				});
			}
		}

		URLClassLoader urlc = new URLClassLoader(new URL[] {});
		return urlc;
	}

	@Override
	public int getServerSocketReadTimeout() {

		return 1000;
	}

}
