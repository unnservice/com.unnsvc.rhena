
package com.unnsvc.rhena.core;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.UniqueList;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class CascadingModelBuilder {

	private IRhenaContext context;
	private IRhenaCache cache;

	public CascadingModelBuilder(IRhenaContext context, IRhenaCache cache) {

		this.cache = cache;
		this.context = context;
	}

	public IRhenaExecution buildEntryPoint(IEntryPoint entryPoint) throws RhenaException {

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

			// Second loop guard to check whether any where selected, if not
			// then we have an error because the selectResolved should always
			// select
			// @TODO move into loopGuard?
			if (resolvable.isEmpty()) {
				for (IEntryPoint edge : allEdges) {
					context.getLogger().debug(getClass(), "Nonresolvable in queue (framework bug): " + edge);
					IRhenaModule module = cache.getModule(edge.getTarget());
					isBuildable(edge, module, true);
				}
				throw new RhenaException("Nonresolvable edges in queue, this is a bug: " + allEdges);
			}

			Runtime runtime = Runtime.getRuntime();
			int threads = context.getConfig().isParallel() ? runtime.availableProcessors() : 1;
			ExecutorService executor = Executors.newFixedThreadPool(threads);

			for (final IEntryPoint edge : resolvable) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						try {
							IRhenaExecution execution = materialiseExecution(edge);
							return execution;
						} catch (Throwable t) {
							context.getLogger().error(getClass(), edge.getTarget(), "Exception in execution materialisation: " + t.getMessage());
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

			for (IEntryPoint relationshipEntryPoint : Utils.getAllEntryPoints(module, true)) {

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

		context.getLogger().info(getClass(), entryPoint.getTarget(), "Building: " + entryPoint.getTarget() + ":" + entryPoint.getExecutionType());

		IRhenaModule module = cache.getModule(entryPoint.getTarget());
		IRhenaExecution execution = module.getRepository().materialiseExecution(cache, entryPoint);

		return execution;
	}

	/**
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
			if (isBuildable(entryPoint, module, false)) {
				selected.add(entryPoint);
				iter.remove();
			}
		}
		return selected;
	}

	private boolean isBuildable(IEntryPoint entryPoint, IRhenaModule module, boolean debug) throws RhenaException {

		boolean buildable = true;
		List<String> waitingOn = new UniqueList<String>();

		if (!module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
			for (ILifecycleProcessorReference ref : module.getLifecycleDeclarations().get(module.getLifecycleName()).getAllReferences()) {
				IEntryPoint lifecycleEntryPoint = ref.getModuleEdge().getEntryPoint();

				if (!cache.containsExecution(lifecycleEntryPoint.getTarget(), lifecycleEntryPoint.getExecutionType())) {
					if (debug) {
						waitingOn.add("↳ lifecycle: " + lifecycleEntryPoint.getTarget() + ":" + lifecycleEntryPoint.getExecutionType().literal());
						buildable = false;
					} else {
						return false;
					}
				}
			}
		}

		// check whether parent execution types have been executed
		// for (EExecutionType et :
		// entryPoint.getExecutionType().getTraversables()) {
		//
		// if (!cache.containsExecution(entryPoint.getTarget(), et)) {
		// if (debug) {
		// waitingOn.add("↳ self: " + entryPoint.getTarget() + ":" +
		// et.literal());
		// buildable = false;
		// } else {
		// return false;
		// }
		// }
		// }

		// up intil, but not including
		for (int i = 0; i < entryPoint.getExecutionType().ordinal(); i++) {
			EExecutionType et = EExecutionType.values()[i];
			if (!cache.containsExecution(entryPoint.getTarget(), et)) {
				if (debug) {
					waitingOn.add("↳ self: " + entryPoint.getTarget() + ":" + et.literal());
					buildable = false;
				} else {
					return false;
				}
			}
		}

		// check whether all relationships have been executed
		for (IRhenaEdge edge : module.getDependencies()) {

			IEntryPoint relEntryPoint = edge.getEntryPoint();
			if (!cache.containsExecution(relEntryPoint.getTarget(), relEntryPoint.getExecutionType())) {
				if (debug) {
					waitingOn.add("↳ dependency: " + relEntryPoint.getTarget() + ":" + relEntryPoint.getExecutionType().literal());
					buildable = false;
				} else {
					return false;
				}
			}
		}

		if (debug) {
			for (String line : waitingOn) {
				context.getLogger().debug(getClass(), "    " + line);
			}
		}

		return buildable;
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
