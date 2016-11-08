
package com.unnsvc.rhena.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class CascadingModelBuilder {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private CascadingModelResolver resolver;
	private CustomThreadPoolExecutor executor;
	private Set<Future<IRhenaExecution>> executing;

	public CascadingModelBuilder(IRhenaConfiguration config, CascadingModelResolver resolver) {

		this.config = config;
		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		this.resolver = resolver;

		this.executor = new CustomThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
		this.executing = Collections.synchronizedSet(new HashSet<Future<IRhenaExecution>>());
	}

	public IRhenaExecution buildEdge(IRhenaEdge entryPoint) throws RhenaException {

		EdgeSet alledges = getAllEdges(entryPoint);
		Set<IRhenaEdge> resolvable = new HashSet<IRhenaEdge>();

		while (loopGuard(alledges)) {

			resolvable = selectResolved(alledges);

			for (final IRhenaEdge edge : resolvable) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						IRhenaExecution execution = materialiseExecution(edge);
						synchronized (CascadingModelBuilder.this) {
							CascadingModelBuilder.this.notifyAll();
						}

						return execution;
					}
				});
			}
		}

		try {
			executor.shutdown();
			// @TODO as this blocks indefinitely, show some debug info or
			// something
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {

			throw new RhenaException(e.getMessage(), e);
		}

		return materialiseExecution(entryPoint);
	}

	private EdgeSet getAllEdges(IRhenaEdge entryPoint) {

		EdgeSet alledges = new EdgeSet();
		alledges.addEdge(entryPoint);
		for (IRhenaModule module : resolver.getModules().values()) {

			for(IRhenaEdge moduleEdge : Utils.getAllRelationships(module)) {
				alledges.addEdge(moduleEdge);
			}
		}
		return alledges;
	}

	/**
	 * This method does one of 3 things:
	 * 
	 * <pre>
	 *	if alledges not empty, enter loop
	 *	if executor is running, block
	 * </pre>
	 * 
	 * @param alledges
	 * @return
	 * @throws RhenaException
	 */
	private boolean loopGuard(Set<IRhenaEdge> alledges) throws RhenaException {

		// If it's already executing, block
		if (executor.isExecuting()) {

			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {

					throw new RhenaException(e.getMessage(), e);
				}
			}
		}

		if (!alledges.isEmpty()) {

			return true;
		} else {

			return false;
		}
	}

	// will be called by multiple threads...
	private IRhenaExecution materialiseExecution(IRhenaEdge edge) throws RhenaException {

		IRhenaExecution execution = produceExecution(edge);
		if (!executions.containsKey(edge.getTarget())) {
			executions.put(edge.getTarget(), new HashMap<EExecutionType, IRhenaExecution>());
		}
		executions.get(edge.getTarget()).put(edge.getExecutionType(), execution);
		return execution;
	}

	private IRhenaExecution produceExecution(IRhenaEdge edge) throws RhenaException {

		System.err.println(getClass().getName() + " Building " + edge.toString());
		IRhenaExecution execution = new RhenaExecution(edge.getTarget(), edge.getExecutionType(), new ArtifactDescriptor("somefile", Utils.toUrl("http://some.url"), "sha1"));
		return execution;
	}

	/**
	 * This method is performance-sensitive so we don't end up in a thread lock
	 * 
	 * @param alledges
	 * @return
	 * @throws RhenaException
	 */
	private Set<IRhenaEdge> selectResolved(Set<IRhenaEdge> alledges) throws RhenaException {

		Set<IRhenaEdge> selected = new HashSet<IRhenaEdge>();
		for (Iterator<IRhenaEdge> iter = alledges.iterator(); iter.hasNext();) {
			IRhenaEdge edge = iter.next();
			IRhenaModule module = resolver.materialiseModel(edge.getTarget());
			if (isBuildable(module)) {
				selected.add(edge);
				iter.remove();
			}
		}
		return selected;
	}

	private boolean isBuildable(IRhenaModule module) throws RhenaException {

		for (IRhenaEdge edge : Utils.getAllRelationships(module)) {
			boolean containsKey = executions.containsKey(edge.getTarget());
			if (!containsKey) {
				return false;
			} else if (containsKey && !executions.get(edge.getTarget()).containsKey(edge.getExecutionType())) {
				return false;
			}
		}

		return true;
	}
}
