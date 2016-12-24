
package com.unnsvc.rhena.core;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class CascadingModelBuilder {

	private IRhenaConfiguration config;
	private IRhenaCache cache;

	public CascadingModelBuilder(IRhenaConfiguration config, IRhenaCache cache) {

		this.cache = cache;
		this.config = config;
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
			int threads = config.isParallel() ? runtime.availableProcessors() : 1;
			ExecutorService executor = Executors.newFixedThreadPool(threads);

			for (final IEntryPoint edge : resolvable) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						try {
							IRhenaExecution execution = materialiseExecution(edge);
							return execution;
						} catch (Throwable t) {
							config.getLogger(getClass()).error(edge.getTarget(), "Exception in execution materialisation: " + t.getMessage());
							t.printStackTrace();
							throw new RhenaException(t.getMessage(), t);
						}
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

		for (IRhenaModule module : cache.getModules().values()) {

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

		IRhenaExecution execution = null;

		if (cache.getExecution(entryPoint.getTarget()).containsKey(entryPoint.getExecutionType())) {

			return cache.getExecution(entryPoint.getTarget()).get(entryPoint.getExecutionType());
		}

		execution = produceExecution(entryPoint);
		cache.getExecution(entryPoint.getTarget()).put(entryPoint.getExecutionType(), execution);
		return execution;
	}

	private IRhenaExecution produceExecution(IEntryPoint entryPoint) throws RhenaException {

		config.getLogger(getClass()).info(entryPoint.getTarget(), "Building: " + entryPoint.getTarget() + ":" + entryPoint.getExecutionType());

		IRhenaModule module = cache.getModule(entryPoint.getTarget());
		IRhenaExecution execution = module.getRepository().materialiseExecution(cache, entryPoint);

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
			IRhenaModule module = cache.getModule(entryPoint.getTarget());
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

			if (!cache.getExecution(entryPoint.getTarget()).containsKey(et)) {
				return false;
			}
		}

		// check whether all relationships have been executed
		for (IEntryPoint relationshipEntryPoint : Utils.getAllEntryPoints(module)) {

			boolean containsModule = cache.getExecutions().containsKey(relationshipEntryPoint.getTarget());
			if (!containsModule) {
				return false;
			} else if (containsModule && !cache.getExecution(relationshipEntryPoint.getTarget()).containsKey(relationshipEntryPoint.getExecutionType())) {
				return false;
			}
		}

		return true;
	}

	private void prefillExecutions(Set<IEntryPoint> alledges) {

		// prefill
		for (IEntryPoint entryPoint : alledges) {
			if (!cache.getExecutions().containsKey(entryPoint.getTarget())) {
				cache.getExecutions().put(entryPoint.getTarget(), new EnumMap<EExecutionType, IRhenaExecution>(EExecutionType.class));
			}
		}
	}
}
