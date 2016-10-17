
package com.unnsvc.rhena.core.execution;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.ModuleType;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class GraphResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Set<ILifecycleDeclaration> lifecycles = new HashSet<ILifecycleDeclaration>();
	private List<IRhenaEdge> processed = new ArrayList<IRhenaEdge>();
	private IRhenaContext context;

	public GraphResolver(IRhenaContext context) {

		this.context = context;
	}

	public void resolveReferences(IRhenaEdge rhenaEdge) throws RhenaException {

		_resolveReferences(rhenaEdge);
	}

	public void _resolveReferences(IRhenaEdge rhenaEdge) throws RhenaException {

		UniqueStack<IRhenaEdge> edges = new UniqueStack<IRhenaEdge>();
		edges.pushUnique(rhenaEdge);

		try {
			while (!edges.isEmpty()) {
				edgeProcessing: {
					IRhenaEdge currentEdge = edges.peek();
					// Always resolve as we visit
					if (currentEdge.getTarget().getModuleType() == ModuleType.REFERENCE) {
						currentEdge.setTarget(context.materialiseModel(currentEdge.getTarget().getModuleIdentifier()));
					}

					// First process parent
					if (currentEdge.getTarget().getParentModule() != null && !processed.contains(currentEdge.getTarget().getParentModule())) {

						edges.pushUnique(currentEdge.getTarget().getParentModule());
						break edgeProcessing;
					}

					if (currentEdge.getTarget().getModuleType() == ModuleType.WORKSPACE) {
						/**
						 * Then process lifecycle if the module is workspace
						 * 
						 **/
						String lifecycleName = currentEdge.getTarget().getLifecycleName();
						if (lifecycleName != null) {
							ILifecycleDeclaration lifecycle = currentEdge.getTarget().getLifecycleDeclaration(lifecycleName);

							/**
							 * @TODO It may already contain a lifecycle which
							 *       was declared somewhere else, but under the
							 *       same name, it won't be re-processed.
							 *       Evalute if this is even possible.
							 */
							if (!lifecycles.contains(lifecycle)) {

								resolveReferences(lifecycle.getContext().getModuleEdge());

								for (IProcessorReference processor : lifecycle.getProcessors()) {

									resolveReferences(processor.getModuleEdge());
								}

								resolveReferences(lifecycle.getGenerator().getModuleEdge());

								lifecycles.add(lifecycle);
							}
						}
					}

					/**
					 * Now we're dealing with the actual dependencies, for this
					 * we will want to only enter requested dependency paths
					 **/

					for (IRhenaEdge dependency : currentEdge.getTarget().getDependencyEdges()) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEdge.getExecutionType().canTraverse(dependency.getExecutionType())) {

							if (!processed.contains(dependency)) {
								edges.pushUnique(dependency);
								break edgeProcessing;
							}
						}
					}

					/**
					 * We're finished with this node so we can pop it
					 */
					edges.pop();
					processed.add(currentEdge);
				}
			}
		} catch (NotUniqueException nue) {
			log.error("Cyclic dependency path detected:");
			boolean shift = false;

			/**
			 * Print something coherent so we can resolve the cycle
			 */
			IRhenaEdge ofInterest = edges.peek();
			boolean startlog = false;
			for (IRhenaEdge edge : edges) {
				if (edge.equals(ofInterest)) {
					startlog = true;
				}
				if (startlog) {
					// @TODO
					log.error("cycle: " + (shift ? "↓" : "↓") + " " + edge.getTarget().getModuleIdentifier().toTag(edge.getExecutionType()));
					shift = !shift;
				}
			}
			throw new RhenaException(nue.getMessage(), nue);
		}
	}

	public Set<ILifecycleDeclaration> getLifecycles() {

		return lifecycles;
	}
}
