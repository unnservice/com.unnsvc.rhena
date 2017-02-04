
package com.unnsvc.rhena.common.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.utils.UniqueStack;

/**
 * New tree visiting mechanism that will only visit relevant tree extents
 * 
 * @author noname
 *
 */
public abstract class AFlatTreeVisitor {

	private ILogger logger;
	private IRhenaCache cache;

	public AFlatTreeVisitor(ILogger logger, IRhenaCache cache) {

		this.logger = logger;
		this.cache = cache;
	}

	public IRhenaModule visitTree(IEntryPoint entryPoint, ESelectionType selectionType) throws RhenaException {

		List<IEntryPoint> processed = new ArrayList<IEntryPoint>();
		UniqueStack<CollectionFrame> tracker = new UniqueStack<CollectionFrame>();
		tracker.push(new CollectionFrame(entryPoint, selectionType));

		try {
			processTracker(tracker, processed);
		} catch (NotUniqueException nue) {
			debugCyclic(entryPoint.getTarget(), tracker, nue);
		}
		return resolveModel(entryPoint.getTarget());
	}

	protected void debugCyclic(ModuleIdentifier identifier, UniqueStack<CollectionFrame> tracker, NotUniqueException nue) throws RhenaException {

		logger.error(getClass(), identifier, "Cyclic dependency path detected:");
		boolean shift = false;

		/**
		 * Print something coherent so we can resolve the cycle
		 */
		IEntryPoint ofInterest = tracker.peek().getEntryPoint();
		boolean startlog = false;
		for (CollectionFrame collectionFrame : tracker) {
			IEntryPoint entryPoint = collectionFrame.getEntryPoint();
			if (entryPoint.equals(ofInterest)) {
				startlog = true;
			}
			if (startlog) {
				// @TODO
				logger.error(getClass(), identifier,
						"Cycle: " + (shift ? "↓" : "↓") + " " + resolveModel(entryPoint.getTarget()).getIdentifier().toTag(entryPoint.getExecutionType()));
				shift = !shift;
			}
		}
		throw new RhenaException(nue.getMessage(), nue);
	}

	private void processTracker(UniqueStack<CollectionFrame> tracker, List<IEntryPoint> processed) throws RhenaException {

		while (!tracker.isEmpty()) {
			edgeProcessing: {
				CollectionFrame currentFrame = tracker.peek();
				IEntryPoint currentEntryPoint = currentFrame.getEntryPoint();
				ESelectionType currentSelectionType = currentFrame.getSelectionType();
				IRhenaModule currentModule = resolveModel(currentEntryPoint.getTarget());

				// if has parent and parent isn't already processed
				if (currentModule.getParent() != null && !processed.contains(currentModule.getParent().getEntryPoint())) {

					IRhenaEdge parentEdge = currentModule.getParent();
					tracker.pushUnique(new CollectionFrame(parentEdge.getEntryPoint(), parentEdge.getTraverseType()));
					break edgeProcessing;
				}

				/**
				 * Resolve lifecycle from here after we've merged parents
				 */
				if (!currentModule.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
					ILifecycleReference lifecycle = currentModule.getMergedLifecycleDeclarations(cache).get(currentModule.getLifecycleName());
					if (lifecycle == null) {
						throw new RhenaException("Could not find lifecycle " + currentModule.getLifecycleName() + " in " + currentModule.getIdentifier());
					}

					for (ILifecycleProcessorReference ref : lifecycle.getAllReferences()) {
						if (!processed.contains(ref.getModuleEdge().getEntryPoint())) {
							IRhenaEdge lifecycleEdge = ref.getModuleEdge();
							tracker.pushUnique(new CollectionFrame(lifecycleEdge.getEntryPoint(), lifecycleEdge.getTraverseType()));
							break edgeProcessing;
						}
					}
				}

				/**
				 * If previous requested a direct, then we don't concern
				 * oursselves with the direct dependencies on the second level
				 */
				if (!currentFrame.getPreviousSelectionType().equals(ESelectionType.DIRECT)) {
					/**
					 * Now we're dealing with the actual dependencies, for this
					 * we will want to only enter requested dependency paths
					 **/
					for (IRhenaEdge dependency : currentModule.getMergedDependencies(cache)) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEntryPoint.getExecutionType().compareTo(dependency.getEntryPoint().getExecutionType()) >= 0) {
							if (!processed.contains(dependency.getEntryPoint())) {

								if (currentSelectionType.equals(ESelectionType.SCOPE)) {

									tracker.pushUnique(new CollectionFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.DIRECT)) {

									tracker.pushUnique(new CollectionFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.COMPONENT)) {

									if (currentModule.getIdentifier().getComponentName().equals(dependency.getEntryPoint().getTarget().getComponentName())) {
										tracker.pushUnique(new CollectionFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
										break edgeProcessing;
									}
								}
							}
						}
					}
				}

				/**
				 * We're finished with this node so we can pop it
				 */
				IEntryPoint resolvedEntryPoint = tracker.pop().getEntryPoint();
				processed.add(resolvedEntryPoint);
				onResolvedEntryPoint(resolvedEntryPoint);
			}
		}

		onAllResolvedEntryPoints(processed);
	}

	protected void onAllResolvedEntryPoints(List<IEntryPoint> resolvedEntryPoints) {
		
		/**
		 * Enables one to hook into the tree traversal and act after each entry
		 * point is resolved
		 */
		int rand = new Random().nextInt();
		for(IEntryPoint ep : resolvedEntryPoints) {
			
			System.err.println(rand + " All " + ep);
		}
	}

	protected void onResolvedEntryPoint(IEntryPoint resolvedEntryPoint) {

		/**
		 * Enables one to hook into the tree traversal and act after each entry
		 * point is resolved
		 */
	}

	protected IRhenaCache getCache() {

		return cache;
	}

	protected IRhenaModule resolveModel(ModuleIdentifier identifier) throws RhenaException {

		return cache.getModule(identifier);
	}

}
