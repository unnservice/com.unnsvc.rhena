
package com.unnsvc.rhena.core;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
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
import com.unnsvc.rhena.core.model.RhenaEdge;

public class CascadingModelBuilder {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private CascadingModelResolver resolver;
	private CustomThreadPoolExecutor executor;

	public CascadingModelBuilder(IRhenaConfiguration config, CascadingModelResolver resolver) {

		this.config = config;
		this.resolver = resolver;

		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		this.executor = new CustomThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
	}

	public IRhenaExecution buildEdge(IRhenaEdge entryPoint) throws RhenaException {

		ExecutionMergeEdgeSet alledges = getAllEdges(entryPoint);
		prefillExecutions(alledges);
		Set<IRhenaEdge> resolvable = new HashSet<IRhenaEdge>();

		while (loopGuard(alledges)) {

			resolvable = selectResolved(alledges);

			for (final IRhenaEdge edge : resolvable) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						// perform execution of parent scopes
						for (EExecutionType type : edge.getExecutionType().getTraversables()) {

							materialiseExecution(new RhenaEdge(type, edge.getTarget(), edge.getTraverseType()));
						}

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

	private ExecutionMergeEdgeSet getAllEdges(IRhenaEdge entryPoint) {

		ExecutionMergeEdgeSet alledges = new ExecutionMergeEdgeSet();
		alledges.addEdge(entryPoint);
		for (IRhenaModule module : resolver.getModules().values()) {

			for (IRhenaEdge moduleEdge : Utils.getAllRelationships(module)) {
				alledges.addEdge(moduleEdge);
			}
		}
		return alledges;
	}

	/**
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

	/**
	 * This will be called from the thread pool
	 * 
	 * @param edge
	 * @return
	 * @throws RhenaException
	 */
	private IRhenaExecution materialiseExecution(IRhenaEdge edge) throws RhenaException {

		// check cache
		IRhenaExecution execution = null;

		if (executions.get(edge.getTarget()).containsKey(edge.getExecutionType())) {

			return executions.get(edge.getTarget()).get(edge.getExecutionType());
		}

		execution = produceExecution(edge);
		executions.get(edge.getTarget()).put(edge.getExecutionType(), execution);
		return execution;
	}

	private IRhenaExecution produceExecution(IRhenaEdge edge) throws RhenaException {

		System.err.println(Thread.currentThread().getName() + " - " + getClass().getName() + " Building " + edge.toString());
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

	private void prefillExecutions(ExecutionMergeEdgeSet alledges) {

		// prefill
		for (IRhenaEdge edge : alledges) {
			if (!executions.containsKey(edge.getTarget())) {
				executions.put(edge.getTarget(), new EnumMap<EExecutionType, IRhenaExecution>(EExecutionType.class));
			}
		}
	}
}
