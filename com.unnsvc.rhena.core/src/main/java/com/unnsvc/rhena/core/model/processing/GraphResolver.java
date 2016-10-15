
package com.unnsvc.rhena.core.model.processing;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.model.RhenaReference;

public class GraphResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<IRhenaEdge> processed = new ArrayList<IRhenaEdge>();
	private IResolutionContext context;

	public GraphResolver(IResolutionContext context) {

		this.context = context;
	}

	public void resolveReferences(IRhenaEdge rhenaEdge) throws RhenaException {

		UniqueStack<IRhenaEdge> edges = new UniqueStack<IRhenaEdge>();
		edges.pushUnique(rhenaEdge);

		while (!edges.isEmpty()) {
			edgeProcessing: {
				IRhenaEdge currentEdge = edges.peek();
				// Always resolve
				if (currentEdge.getTarget() instanceof RhenaReference) {
					currentEdge.setTarget(context.materialiseModel(currentEdge.getTarget().getModuleIdentifier()));
				}

				// First process parent
				if (currentEdge.getTarget().getParentModule() != null && !processed.contains(currentEdge.getTarget().getParentModule())) {

					edges.pushUnique(currentEdge.getTarget().getParentModule());
					break edgeProcessing;
				}

				/**
				 * Then process lifecycle, but only the one the target module
				 * references
				 **/
				if (currentEdge.getTarget().getLifecycleName() != null) {
					ILifecycleDeclaration lifecycle = currentEdge.getTarget().getLifecycleDeclaration(currentEdge.getTarget().getLifecycleName());

					if (!processed.contains(lifecycle.getContext())) {
						edges.pushUnique(lifecycle.getContext());
						break edgeProcessing;
					}

					for (IProcessorReference processor : lifecycle.getProcessors()) {
						if (!processed.contains(processor)) {
							edges.pushUnique(processor);
							break edgeProcessing;
						}
					}

					if (!processed.contains(lifecycle.getGenerator())) {
						edges.pushUnique(lifecycle.getGenerator());
						break edgeProcessing;
					}
				}

				/**
				 * Now we're dealing with the actual dependencies, for this we
				 * will want to only enter requested dependency paths
				 **/

				for (IRhenaEdge dependency : currentEdge.getTarget().getDependencyEdges()) {

					/**
					 * We only care about dependencies which we can use in the
					 * requested scope
					 */
					if (dependency.getExecutionType().equals(currentEdge.getExecutionType())) {
						log.info(currentEdge.getExecutionType().getClass().getName() + " isInstance on -> " + dependency.getExecutionType().getClass().getName());
						if (!processed.contains(dependency)) {

							edges.pushUnique(dependency);
							break edgeProcessing;
						}
					} else {
						log.info(dependency.getExecutionType().getClass().getName() + " not assignable to " + currentEdge.getExecutionType().getClass());
					}
				}

				/**
				 * We're finished with this node so we can pop it
				 */
				edges.pop();
				processed.add(currentEdge);
			}
		}
	}
}
