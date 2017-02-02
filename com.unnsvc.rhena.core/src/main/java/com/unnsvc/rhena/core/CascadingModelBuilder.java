
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

import com.unnsvc.rhena.common.ICaller;
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

	public IRhenaExecution buildEntryPoint(ICaller caller) throws RhenaException {

		/**
		 * Refactor to traverse tree and only get relevant entry points
		 */
		ExecutionMergeEntryPoint allEntryPoints = getAllEntryPoints(caller.getEntryPoint());
		prefillExecutions(allEntryPoints);
		Set<IEntryPoint> resolvableEntryPoints = new HashSet<IEntryPoint>();

		while (loopGuard(allEntryPoints)) {

			resolvableEntryPoints = selectResolved(allEntryPoints);

			// Second loop guard to check whether any where selected, if not
			// then we have an error because the selectResolved should always
			// select
			// @TODO move into loopGuard?
			if (resolvableEntryPoints.isEmpty()) {
				for (IEntryPoint edge : allEntryPoints) {
					context.getLogger().debug(getClass(), "Nonresolvable in queue (framework bug): " + edge);
					IRhenaModule module = cache.getModule(edge.getTarget());
					isBuildable(edge, module, true);
				}
				throw new RhenaException("Nonresolvable edges in queue, this is a bug: " + allEntryPoints);
			}

			Runtime runtime = Runtime.getRuntime();
			int threads = context.getConfig().isParallel() ? runtime.availableProcessors() : 1;
			ExecutorService executor = Executors.newFixedThreadPool(threads);

			for (final IEntryPoint resolvableEntryPoint : resolvableEntryPoints) {

				executor.submit(new Callable<IRhenaExecution>() {

					@Override
					public IRhenaExecution call() throws Exception {

						try {
							/**
							 * When it's the caller, produce execution witht he
							 * same caller object, otherwise create an internal
							 * caller object
							 */
							if (caller.getEntryPoint().equals(resolvableEntryPoint)) {
								IRhenaExecution execution = materialiseExecution(caller);
								return execution;
							} else {
								IRhenaModule module = cache.getModule(resolvableEntryPoint.getTarget());
								ICaller internalCaller = new InternalCaller(module, resolvableEntryPoint.getExecutionType());
								IRhenaExecution execution = materialiseExecution(internalCaller);
								return execution;
							}
						} catch (Throwable t) {
							context.getLogger().error(getClass(), resolvableEntryPoint.getTarget(), "Exception in execution materialisation: " + t.getMessage());
							t.printStackTrace();
							throw new RhenaException(t.getMessage(), t);
						}
					}
				});
			}

			/**
			 * @TODO It might be so that a lifecycle blocks because of bad
			 *       code/bugs/jvm oom, so there might need to be a timeout of
			 *       some kind, maybe by polling the agent after a few seconds.
			 */
			try {
				executor.shutdown();
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {

				throw new RhenaException(e.getMessage(), e);
			}

		}

		return materialiseExecution(caller);
	}

	/**
	 * 
	 * @param entryPoint
	 * @return
	 */
	private ExecutionMergeEntryPoint getAllEntryPoints(IEntryPoint entryPoint) {

		ExecutionMergeEntryPoint allEntryPoints = new ExecutionMergeEntryPoint();
		allEntryPoints.addEntryPoint(entryPoint);

		for (IRhenaModule module : cache.getModules().values()) {

			for (IEntryPoint relationshipEntryPoint : Utils.getAllEntryPoints(cache, module, true)) {

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
	 * This will be called in the thread pool
	 * 
	 * @param edge
	 * @return
	 * @throws RhenaException
	 */
	private IRhenaExecution materialiseExecution(ICaller caller) throws RhenaException {

		IRhenaExecution execution = null;

		if (cache.getExecution(caller.getIdentifier()).containsKey(caller.getExecutionType())) {

			return cache.getExecution(caller.getIdentifier()).get(caller.getExecutionType());
		}

		execution = produceExecution(caller);
		cache.getExecution(caller.getIdentifier()).put(caller.getExecutionType(), execution);
		return execution;
	}

	private IRhenaExecution produceExecution(ICaller caller) throws RhenaException {

		context.getLogger().info(getClass(), caller.getIdentifier(), "Building: " + caller.getExecutionType().toString().toLowerCase());

		IRhenaExecution execution = caller.getModule().getRepository().materialiseExecution(cache, caller);

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
			for (ILifecycleProcessorReference ref : module.getMergedLifecycleDeclarations(cache).get(module.getLifecycleName()).getAllReferences()) {
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
		for (IRhenaEdge edge : module.getMergedDependencies(cache)) {

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
