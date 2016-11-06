
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.UniqueStack;

/**
 * This class will also check whether there are any cyclic relationships
 * 
 * @author noname
 *
 */
public class CascadingModelResolver {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, IRhenaModule> modules;
	private List<IRhenaEdge> processed;

	public CascadingModelResolver(IRhenaConfiguration config) {

		this.config = config;
		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.processed = new ArrayList<IRhenaEdge>();
	}

	/**
	 * Cascading resolver which performs cyclic checking
	 * 
	 * @param entryPoint
	 * @throws RhenaException
	 */
	public IRhenaModule resolveEdge(IRhenaEdge entryPoint) throws RhenaException {

		UniqueStack<IRhenaEdge> tracker = new UniqueStack<IRhenaEdge>();
		tracker.push(entryPoint);

		try {
			while (!tracker.isEmpty()) {
				edgeProcessing: {
					IRhenaEdge currentEdge = tracker.peek();
					IRhenaModule currentModule = materialiseModel(currentEdge.getTarget());

					// if has parent and parent isn't already processed
					if (currentModule.getParent() != null && !processed.contains(currentModule.getParent())) {

						tracker.pushUnique(currentModule.getParent());
						break edgeProcessing;
					}

					/**
					 * Now we're dealing with the actual dependencies, for this
					 * we will want to only enter requested dependency paths
					 **/

					for (IRhenaEdge dependency : currentModule.getDependencies()) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEdge.getExecutionType().canTraverse(dependency.getExecutionType())) {

							if (!processed.contains(dependency)) {
								tracker.pushUnique(dependency);
								break edgeProcessing;
							}
						}
					}

					/**
					 * We're finished with this node so we can pop it
					 */
					processed.add(tracker.pop());

				}
			}
		} catch (NotUniqueException nue) {
			System.err.println(getClass() + " Cyclic dependency path detected:");
			boolean shift = false;

			/**
			 * Print something coherent so we can resolve the cycle
			 */
			IRhenaEdge ofInterest = tracker.peek();
			boolean startlog = false;
			for (IRhenaEdge edge : tracker) {
				if (edge.equals(ofInterest)) {
					startlog = true;
				}
				if (startlog) {
					// @TODO
					System.err.println(getClass() + " - cycle: " + (shift ? "↓" : "↓") + " "
							+ materialiseModel(edge.getTarget()).getIdentifier().toTag(edge.getExecutionType()));
					shift = !shift;
				}
			}
			throw new RhenaException(nue.getMessage(), nue);
		}

		return materialiseModel(entryPoint.getTarget());
	}

	private IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = modules.get(identifier);
		if (module == null) {

			// initial module resolve
			for (IRepository repository : config.getRepositories()) {

				try {
					module = repository.materialiseModel(identifier);
					if (module != null) {
						break;
					}
				} catch (NotExistsException nee) {
					// no-op
				}
			}

			if (module == null) {
				throw new RhenaException(identifier.toString() + " not found");
			}

			modules.put(identifier, module);
		}
		return module;
	}

	public List<IRhenaEdge> getAllEdges() {

		return processed;
	}
}
