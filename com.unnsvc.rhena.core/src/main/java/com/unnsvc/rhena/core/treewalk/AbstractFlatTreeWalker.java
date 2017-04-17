
package com.unnsvc.rhena.core.treewalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.ILifecycleConfiguration;
import com.unnsvc.rhena.common.ng.model.ILifecycleReference;
import com.unnsvc.rhena.common.ng.model.IRhenaEdge;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.utils.UniqueStack;
import com.unnsvc.rhena.model.UnresolvedLifecycleConfiguration;

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
		return onResolveModule(entryPoint.getTarget());
	}

	private void processTracker(UniqueStack<FlatTreeFrame> tracker, List<IEntryPoint> processed) throws RhenaException {

		while (!tracker.isEmpty()) {
			edgeProcessing: {
				FlatTreeFrame currentFrame = tracker.peek();
				IEntryPoint currentEntryPoint = currentFrame.getEntryPoint();
				ESelectionType currentSelectionType = currentFrame.getSelectionType();
				IRhenaModule currentModule = onResolveModule(currentEntryPoint.getTarget());

				/**
				 * Check parent relationship
				 */
				if (currentModule.getParent() != null && !processed.contains(currentModule.getParent().getEntryPoint())) {

					IRhenaEdge parentEdge = currentModule.getParent();
					tracker.pushUnique(new FlatTreeFrame(parentEdge.getEntryPoint(), parentEdge.getTraverseType()));
					break edgeProcessing;
				}

				onParentResolved(currentModule);

				/**
				 * Check lifecycle relationships
				 */
				if (!currentModule.getLifecycleConfiguration().getName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
					for (ILifecycleReference ref : currentModule.getLifecycleConfiguration()) {
						if (!processed.contains(ref.getEntryPoint())) {
							onRelationship(currentModule, ref.getEntryPoint());
							tracker.pushUnique(new FlatTreeFrame(ref.getEntryPoint(), ref.getTraverseType()));
							break edgeProcessing;
						}
					}
				}

				/**
				 * Check dependencies
				 * 
				 * If previous requested a direct, then we don't concern
				 * oursselves with the direct dependencies on the second level
				 */
				if (!currentFrame.getPreviousSelectionType().equals(ESelectionType.DIRECT)) {
					/**
					 * Now we're dealing with the actual dependencies, for this
					 * we will want to only enter requested dependency paths
					 **/
					for (IRhenaEdge dependency : getMergedDependencies(currentModule)) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEntryPoint.getExecutionType().compareTo(dependency.getEntryPoint().getExecutionType()) >= 0) {
							if (!processed.contains(dependency.getEntryPoint())) {

								if (currentSelectionType.equals(ESelectionType.SCOPE)) {

									onRelationship(currentModule, dependency.getEntryPoint());
									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.DIRECT)) {

									onRelationship(currentModule, dependency.getEntryPoint());
									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else if (currentSelectionType.equals(ESelectionType.COMPONENT)) {

									if (currentModule.getIdentifier().getComponentName().equals(dependency.getEntryPoint().getTarget().getComponentName())) {
										onRelationship(currentModule, dependency.getEntryPoint());
										tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
										break edgeProcessing;
									}
								}
							}
						}
					}
				}

				/**
				 * We're finished with this node so we can consider it processed
				 */
				FlatTreeFrame frame = tracker.pop();
				IEntryPoint resolvedEntryPoint = frame.getEntryPoint();
				processed.add(resolvedEntryPoint);
			}
		}
	}

	protected void onRelationship(IRhenaModule source, IEntryPoint target) {

	}

	protected List<IRhenaEdge> getMergedDependencies(IRhenaModule currentModule) throws RhenaException {

		Stack<IRhenaModule> moduleChain = new Stack<IRhenaModule>();
		IRhenaModule cursorModule = currentModule;
		while (cursorModule != null) {
			moduleChain.push(cursorModule);
			IRhenaEdge parent = cursorModule.getParent();
			
			if(parent != null) {
				cursorModule = onResolveModule(parent.getEntryPoint().getTarget());
			} else {
				break;
			}
		}

		// now merge dependencies
		List<IRhenaEdge> dependencies = new ArrayList<IRhenaEdge>();
		while (!moduleChain.isEmpty()) {
			dependencies.addAll(moduleChain.pop().getDependencies());
		}
		return dependencies;
	}

	/**
	 * On parent resolved but before lifecycles, lifecycle declarations, and
	 * dependencies
	 * 
	 * @param currentModule
	 * @throws RhenaException
	 */
	protected void onParentResolved(IRhenaModule currentModule) throws RhenaException {

		/**
		 * Resolve its lifecycle
		 * 
		 */
		if (currentModule.getLifecycleConfiguration() instanceof UnresolvedLifecycleConfiguration) {

			ILifecycleConfiguration config = null;
			IRhenaModule cursorModule = currentModule;
			while (config == null && cursorModule != null) {

				/**
				 * It's a custom lifecycle so we want to search for its
				 * declaration in the hierarchy
				 */
				for (ILifecycleConfiguration declaredConfig : currentModule.getDeclaredConfigurations()) {

					if (declaredConfig.getName().equals(currentModule.getLifecycleConfiguration().getName())) {
						config = declaredConfig;
						break;
					}
				}

				cursorModule = onResolveModule(cursorModule.getParent().getEntryPoint().getTarget());
			}

			if (config == null) {
				throw new RhenaException(
						"Lifecycle " + currentModule.getLifecycleConfiguration().getName() + " not found for " + currentModule.getIdentifier());
			}

			currentModule.setLifecycleConfiguration(config);
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
				IRhenaModule module = onResolveModule(entryPoint.getTarget());
				// @TODO more coherent debugging of cyclig model error
				logger.error("Cycle: " + (shift ? "↓" : "↓") + " " + module.getIdentifier().toTag(entryPoint.getExecutionType()));
				shift = !shift;
			}
		}
	}

	protected abstract IRhenaModule onResolveModule(ModuleIdentifier identifier) throws RhenaException;

}