
package com.unnsvc.rhena.common.traversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.UnresolvedLifecycleSpecification;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.ILifecycleSpecification;
import com.unnsvc.rhena.common.model.IProcessorSpec;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.utils.UniqueStack;

/**
 * Flat tree walker walks the tree without recursion and provides API hooks
 * 
 * @author noname
 *
 */
public abstract class AbstractFlatTreeWalker {

	private Logger log = LoggerFactory.getLogger(getClass());

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
		IRhenaModule module = onResolveModule(entryPoint.getTarget());
		onRelationship(null, entryPoint);

		/**
		 * 
		 */
		onTraversalComplete();
		return module;
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
				} else {

					onParentResolved(currentModule);
				}

				/**
				 * Check lifecycle relationships
				 */
				if (!currentModule.getLifecycleSpecification().getName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {

					for (IRhenaEdge lifecycleEdge : currentModule.getLifecycleSpecification()) {

						if (!processed.contains(lifecycleEdge.getEntryPoint())) {
							tracker.pushUnique(new FlatTreeFrame(lifecycleEdge.getEntryPoint(), lifecycleEdge.getTraverseType()));
							break edgeProcessing;
						} else {
							/**
							 * Call this once we know this relationship has been
							 * processed
							 */
							onRelationship(currentModule, lifecycleEdge.getEntryPoint());
						}
					}

					for (IProcessorSpec processorRef : currentModule.getLifecycleSpecification().processorIterator()) {

						for (IRhenaEdge processorRefDep : processorRef) {

							if (!processed.contains(processorRefDep.getEntryPoint())) {
								tracker.pushUnique(new FlatTreeFrame(processorRefDep.getEntryPoint(), processorRefDep.getTraverseType()));
								break edgeProcessing;
							} else {
								/**
								 * Call this once we know this relationship has
								 * been processed
								 */
								onRelationship(currentModule, processorRefDep.getEntryPoint());
							}
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
						if (dependency.getEntryPoint().getExecutionType().lessOrEqualTo(currentEntryPoint.getExecutionType())) {
							traceSelector("->", currentEntryPoint, dependency);

							if (currentSelectionType.equals(ESelectionType.SCOPE)) {

								if (!processed.contains(dependency.getEntryPoint())) {

									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else {
									onRelationship(currentModule, dependency.getEntryPoint());
								}
							} else if (currentSelectionType.equals(ESelectionType.DIRECT)) {

								if (!processed.contains(dependency.getEntryPoint())) {

									tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
									break edgeProcessing;
								} else {
									onRelationship(currentModule, dependency.getEntryPoint());
								}
							} else if (currentSelectionType.equals(ESelectionType.COMPONENT)) {

								/**
								 * Need to check processed inside each if else
								 * exactly because of this relationship which
								 * may or may not lead to an onRelationship()
								 * call
								 */
								if (currentModule.getIdentifier().getComponentName().equals(dependency.getEntryPoint().getTarget().getComponentName())) {
									if (!processed.contains(dependency.getEntryPoint())) {

										tracker.pushUnique(new FlatTreeFrame(dependency.getEntryPoint(), dependency.getTraverseType(), currentSelectionType));
										break edgeProcessing;
									} else {
										onRelationship(currentModule, dependency.getEntryPoint());
									}
								}
							}

						} else {

							traceSelector("-/>", currentEntryPoint, dependency);
						}
					}
				}

				/**
				 * We're finished with this node so we can consider it processed
				 */
				FlatTreeFrame frame = tracker.pop();
				IEntryPoint resolvedEntryPoint = frame.getEntryPoint();
				processed.add(resolvedEntryPoint);
				onModuleResolved(currentModule);
			}
		}
	}

	private void traceSelector(String string, IEntryPoint currentEntryPoint, IRhenaEdge dependency) {

		String src = currentEntryPoint.getTarget().toString() + ":" + currentEntryPoint.getExecutionType().toString().toLowerCase();
		String dep = dependency.getEntryPoint().getTarget().toString() + ":" + dependency.getEntryPoint().getExecutionType().toString().toLowerCase();
		log.trace("relationship: " + src + " " + string + " dependency " + dep);
	}

	/**
	 * This is called once the traversal of all nodes and relationships is
	 * complete
	 * 
	 * @throws InterruptedException
	 */
	protected void onTraversalComplete() throws RhenaException {

	}

	/**
	 * This is called once after each module is resolved in that it has all of
	 * its outgoing modules processed nd in cache
	 */
	protected void onModuleResolved(IRhenaModule resolvedModule) throws RhenaException {

	}

	/**
	 * This is called after a found relationship is known to be in a processed
	 * state, may be called multiple times for the same relationship as it is
	 * found in the model
	 * 
	 * @param source
	 * @param entryPoint
	 * @throws RhenaException
	 */
	protected void onRelationship(IRhenaModule source, IEntryPoint outgoing) throws RhenaException {

	}

	protected List<IRhenaEdge> getMergedDependencies(IRhenaModule currentModule) throws RhenaException {

		Stack<IRhenaModule> moduleChain = new Stack<IRhenaModule>();
		IRhenaModule cursorModule = currentModule;
		while (cursorModule != null) {
			moduleChain.push(cursorModule);
			IRhenaEdge parent = cursorModule.getParent();

			if (parent != null) {
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
		if (currentModule.getLifecycleSpecification() instanceof UnresolvedLifecycleSpecification) {

			ILifecycleSpecification lifecycleSpecification = null;
			IRhenaModule cursorModule = currentModule;
			while (lifecycleSpecification == null && cursorModule != null) {

				/**
				 * It's a custom lifecycle so we want to search for its
				 * declaration in the hierarchy
				 */
				for (ILifecycleSpecification declaredConfig : currentModule.getDeclaredLifecycleSpecifications()) {

					if (declaredConfig.getName().equals(currentModule.getLifecycleSpecification().getName())) {
						lifecycleSpecification = declaredConfig;
						break;
					}
				}

				cursorModule = onResolveModule(cursorModule.getParent().getEntryPoint().getTarget());
			}

			if (lifecycleSpecification == null) {
				throw new RhenaException("Lifecycle " + currentModule.getLifecycleSpecification().getName() + " not found for " + currentModule.getIdentifier());
			}

			currentModule.setLifecycleSpecification(lifecycleSpecification);
		}
	}

	protected void debugCyclic(ModuleIdentifier identifier, UniqueStack<FlatTreeFrame> tracker) throws RhenaException {

		log.error("Cyclic dependency path detected:");
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
				log.error("Cycle: " + (shift ? "↓" : "↓") + " " + module.getIdentifier().toTag(entryPoint.getExecutionType()));
				shift = !shift;
			}
		}
	}

	protected abstract IRhenaModule onResolveModule(ModuleIdentifier identifier) throws RhenaException;

}
