
package com.unnsvc.rhena.core.treewalk;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.ILifecycleReference;
import com.unnsvc.rhena.common.ng.model.IRhenaEdge;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.utils.UniqueStack;

public abstract class AbstractFlatTreeWalker {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public AbstractFlatTreeWalker() {

	}

	/**
	 * Will visit entry point and its dependencies
	 * 
	 * @param entryPoint
	 * @param selectionType
	 * @return
	 * @throws RhenaException
	 */
	public IRhenaModule visitTree(IEntryPoint entryPoint, ESelectionType selectionType) throws RhenaException {

		List<IEntryPoint> processed = new ArrayList<IEntryPoint>();
		UniqueStack<FlatTreeFrame> tracker = new UniqueStack<FlatTreeFrame>();
		tracker.push(new FlatTreeFrame(entryPoint, selectionType));

		try {
			processTracker(tracker, processed);
		} catch (NotUniqueException nue) {
			debugCyclic(entryPoint.getTarget(), tracker);
			throw new RhenaException(nue.getMessage(), nue);
		}
		return resolveModule(entryPoint.getTarget());
	}

	private void processTracker(UniqueStack<FlatTreeFrame> tracker, List<IEntryPoint> processed) throws RhenaException {

		while (!tracker.isEmpty()) {
			edgeProcessing: {
				FlatTreeFrame currentFrame = tracker.peek();
				IEntryPoint currentEntryPoint = currentFrame.getEntryPoint();
				ESelectionType currentSelectionType = currentFrame.getSelectionType();
				IRhenaModule currentModule = resolveModule(currentEntryPoint.getTarget());

				// if has parent and parent isn't already processed
				if (currentModule.getParent() != null && !processed.contains(currentModule.getParent().getEntryPoint())) {

					IRhenaEdge parentEdge = currentModule.getParent();
					tracker.pushUnique(new FlatTreeFrame(parentEdge.getEntryPoint(), parentEdge.getTraverseType()));
					break edgeProcessing;
				}

				/**
				 * Resolve lifecycle from here after we've merged parents
				 */
				if (currentModule.getLifecycleConfiguration() != null) {

					for (ILifecycleReference ref : currentModule.getLifecycleConfiguration()) {
						if (!processed.contains(ref.getEntryPoint())) {
							tracker.pushUnique(new FlatTreeFrame(ref.getEntryPoint(), ref.getTraverseType()));
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
					for (IRhenaEdge dependency : currentModule) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEntryPoint.getExecutionType().compareTo(dependency.getEntryPoint().getExecutionType()) >= 0) {
							if (!processed.contains(dependency.getEntryPoint())) {

								if (currentSelectionType.equals(ESelectionType.SCOPE)) {

									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.DIRECT)) {

									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.COMPONENT)) {

									if (currentModule.getIdentifier().getComponentName().equals(dependency.getEntryPoint().getTarget().getComponentName())) {
										tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
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
				FlatTreeFrame frame = tracker.pop();
				IEntryPoint resolvedEntryPoint = frame.getEntryPoint();
				processed.add(resolvedEntryPoint);
			}
		}
	}

	protected void debugCyclic(ModuleIdentifier identifier, UniqueStack<FlatTreeFrame> tracker) throws RhenaException {

		logger.error("Cyclic dependency path detected:");
		boolean shift = false;

		/**
		 * Print something coherent so we can resolve the cycle
		 */
		IEntryPoint ofInterest = tracker.peek().getEntryPoint();
		boolean startlog = false;
		for (FlatTreeFrame collectionFrame : tracker) {
			IEntryPoint entryPoint = collectionFrame.getEntryPoint();
			if (entryPoint.equals(ofInterest)) {
				startlog = true;
			}
			if (startlog) {
				// @TODO
				logger.error(
						"Cycle: " + (shift ? "↓" : "↓") + " " + resolveModule(entryPoint.getTarget()).getIdentifier().toTag(entryPoint.getExecutionType()));
				shift = !shift;
			}
		}
	}

	protected abstract IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException;

}
