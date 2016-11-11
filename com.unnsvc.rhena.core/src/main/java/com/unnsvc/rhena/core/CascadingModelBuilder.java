
package com.unnsvc.rhena.core;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class CascadingModelBuilder {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private IModelResolver resolver;
	// private CustomThreadPoolExecutor executor;

	public CascadingModelBuilder(IRhenaConfiguration config, IModelResolver resolver) {

		this.config = config;
		this.resolver = resolver;

		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		// this.executor = new
		// CustomThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
	}

	public IRhenaExecution buildEdge(IEntryPoint entryPoint) throws RhenaException {

		ExecutionMergeEdgeSet allEdges = getAllEntryPoints(entryPoint);
		prefillExecutions(allEdges);
		Set<IEntryPoint> resolvable = new HashSet<IEntryPoint>();

		/**
		 * @TODO we want more efficient threading, instead of waiting for each n
		 *       number of threads to complete, release the thread block after
		 *       each thread completion so we can feed the thread pool
		 *       continuously
		 */
		while (loopGuard(allEdges)) {

			resolvable = selectResolved(allEdges);

			Runtime runtime = Runtime.getRuntime();
			int threads = resolvable.size() > runtime.availableProcessors() ? runtime.availableProcessors() : resolvable.size();
			if (!config.isParallel()) {
				threads = 1;
			}
			ExecutorService executor = Executors.newFixedThreadPool(threads);

			for (final IEntryPoint edge : resolvable) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						IRhenaExecution execution = materialiseExecution(edge);
						return execution;
					}
				});
			}

			/**
			 * Theory is that this can't wait indefinitely because the model is
			 * checked for cycles so it can resolve.
			 */
			try {
				executor.shutdown();
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {

				throw new RhenaException(e.getMessage(), e);
			}

		}

		return materialiseExecution(entryPoint);
	}

	/**
	 * 
	 * @param entryPoint
	 * @return
	 */
	private ExecutionMergeEdgeSet getAllEntryPoints(IEntryPoint entryPoint) {

		ExecutionMergeEdgeSet allEntryPoints = new ExecutionMergeEdgeSet();
		allEntryPoints.addEntryPoint(entryPoint);

		for (IRhenaModule module : resolver.getModules().values()) {

			for (IEntryPoint relationshipEntryPoint : Utils.getAllEntryPoints(module)) {

				allEntryPoints.addEntryPoint(relationshipEntryPoint);
			}
		}
		return allEntryPoints;
	}

	/**
	 * 
	 * @param allEntryPoints
	 * @return
	 * @throws RhenaException
	 */
	private boolean loopGuard(Set<IEntryPoint> allEntryPoints) throws RhenaException {

		if (!allEntryPoints.isEmpty()) {

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
	private IRhenaExecution materialiseExecution(IEntryPoint entryPoint) throws RhenaException {

		// check cache
		IRhenaExecution execution = null;

		if (executions.get(entryPoint.getTarget()).containsKey(entryPoint.getExecutionType())) {

			return executions.get(entryPoint.getTarget()).get(entryPoint.getExecutionType());
		}

		execution = produceExecution(entryPoint);
		executions.get(entryPoint.getTarget()).put(entryPoint.getExecutionType(), execution);
		return execution;
	}

	private IRhenaExecution produceExecution(IEntryPoint entryPoint) throws RhenaException {
		
		System.err.println(Thread.currentThread().getName() + ":" + getClass().getName() + " Building: " + entryPoint.getTarget() + ":" + entryPoint.getExecutionType());
		
		IRhenaModule module = resolver.materialiseModel(entryPoint.getTarget());
		IRhenaExecution execution = module.getRepository().materialiseExecution(resolver, entryPoint);

		return execution;
	}

	/**
	 * This method is performance-sensitive so we don't end up in a thread lock
	 * 
	 * @param alledges
	 * @return
	 * @throws RhenaException
	 */
	private Set<IEntryPoint> selectResolved(Set<IEntryPoint> alledges) throws RhenaException {

		Set<IEntryPoint> selected = new HashSet<IEntryPoint>();
		for (Iterator<IEntryPoint> iter = alledges.iterator(); iter.hasNext();) {
			IEntryPoint entryPoint = iter.next();
			IRhenaModule module = resolver.materialiseModel(entryPoint.getTarget());
			if (isBuildable(entryPoint, module)) {
				selected.add(entryPoint);
				iter.remove();
			}
		}
		return selected;
	}

	private boolean isBuildable(IEntryPoint entryPoint, IRhenaModule module) throws RhenaException {

		// check whether parent execution types have been executed
		for (EExecutionType et : entryPoint.getExecutionType().getTraversables()) {

			if (!executions.get(entryPoint.getTarget()).containsKey(et)) {
				return false;
			}
		}

		// check whether all relationships have been executed
		for (IEntryPoint relationshipEntryPoint : Utils.getAllEntryPoints(module)) {

			boolean containsModule = executions.containsKey(relationshipEntryPoint.getTarget());
			if (!containsModule) {
				return false;
			} else if (containsModule && !executions.get(relationshipEntryPoint.getTarget()).containsKey(relationshipEntryPoint.getExecutionType())) {
				return false;
			}
		}

		return true;
	}

	private void prefillExecutions(Set<IEntryPoint> alledges) {

		// prefill
		for (IEntryPoint entryPoint : alledges) {
			if (!executions.containsKey(entryPoint.getTarget())) {
				executions.put(entryPoint.getTarget(), new EnumMap<EExecutionType, IRhenaExecution>(EExecutionType.class));
			}
		}
	}
}
