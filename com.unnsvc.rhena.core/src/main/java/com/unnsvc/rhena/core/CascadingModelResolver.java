
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.core.execution.UniqueStack;

/**
 * This class will also check whether there are any cyclic relationships
 * 
 * @author noname
 *
 */
public class CascadingModelResolver {

	private IRhenaConfiguration config;
	private Set<ModuleIdentifier> merged;
	private IRhenaCache cache;

	public CascadingModelResolver(IRhenaConfiguration config, IRhenaCache cache) {

		this.config = config;
		this.merged = new HashSet<ModuleIdentifier>();
		this.cache = cache;
	}

	/**
	 * Cascading resolver which performs cyclic checking.
	 * 
	 * @TODO make bolognese out of this pasta, factor out the breaks into method
	 *       returns
	 * @param entryPoint
	 * @throws RhenaException
	 */
	public IRhenaModule resolveEdge(IEntryPoint entryPoint) throws RhenaException {

		List<IEntryPoint> processed = new ArrayList<IEntryPoint>();
		UniqueStack<IEntryPoint> tracker = new UniqueStack<IEntryPoint>();
		tracker.push(entryPoint);

		try {
			while (!tracker.isEmpty()) {
				edgeProcessing: {
					IEntryPoint currentEntryPoint = tracker.peek();
					IRhenaModule currentModule = materialiseModel(currentEntryPoint.getTarget());

					// if has parent and parent isn't already processed
					if (currentModule.getParent() != null && !processed.contains(currentModule.getParent().getEntryPoint())) {

						tracker.pushUnique(currentModule.getParent().getEntryPoint());
						break edgeProcessing;
					}

					/**
					 * @TODO evaluate impact. We don't want to merge a third
					 *       child before a second if they both end up in the
					 *       tracker. Can this even occur?
					 * @TODO really shouldn't perform model merge right in the
					 *       middle of the model resolution cascade, but
					 *       necessary at present to make this work.
					 */
					if (currentModule.getParent() != null && !merged.contains(currentModule.getIdentifier())) {
						IRhenaModule parentModule = materialiseModel(currentModule.getParent().getEntryPoint().getTarget());
						merge(currentModule, parentModule);
						merged.add(currentModule.getIdentifier());
					}

					/**
					 * Resolve lifecycle from here after we've merged parents
					 */
					if (currentModule.getLifecycleName() != null) {
						ILifecycleReference lifecycle = currentModule.getLifecycleDeclarations().get(currentModule.getLifecycleName());
						if (lifecycle == null) {
							throw new RhenaException("Could not find lifecycle " + currentModule.getLifecycleName() + " in " + currentModule.getIdentifier());
						}

						for (ILifecycleProcessorReference ref : lifecycle.getAllReferences()) {
							if (!processed.contains(ref.getModuleEdge().getEntryPoint())) {
								tracker.pushUnique(ref.getModuleEdge().getEntryPoint());
								break edgeProcessing;
							}
						}
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
						if (currentEntryPoint.getExecutionType().canTraverse(dependency.getEntryPoint().getExecutionType())) {

							if (!processed.contains(dependency.getEntryPoint())) {
								tracker.pushUnique(dependency.getEntryPoint());
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

			// debug. This shouldn't be needed but might useful for evaluating
			// bugs in the cyclic check
			// System.err.println("Processing: " + processed.size());
			// for(IEntryPoint ep : processed) {
			// System.err.println(" ep " + ep);
			// }

		} catch (NotUniqueException nue) {
			config.getLogger().error(getClass(), entryPoint.getTarget(), "Cyclic dependency path detected:");
			boolean shift = false;

			/**
			 * Print something coherent so we can resolve the cycle
			 */
			IEntryPoint ofInterest = tracker.peek();
			boolean startlog = false;
			for (IEntryPoint edge : tracker) {
				if (edge.equals(ofInterest)) {
					startlog = true;
				}
				if (startlog) {
					// @TODO
					config.getLogger().error(getClass(), entryPoint.getTarget(),
							"Cycle: " + (shift ? "↓" : "↓") + " " + materialiseModel(edge.getTarget()).getIdentifier().toTag(edge.getExecutionType()));
					shift = !shift;
				}
			}
			throw new RhenaException(nue.getMessage(), nue);
		}

		return materialiseModel(entryPoint.getTarget());
	}

	private void merge(IRhenaModule currentModule, IRhenaModule parentModule) {

		// merge dependencies
		List<IRhenaEdge> dependencies = new ArrayList<IRhenaEdge>();
		dependencies.addAll(parentModule.getDependencies());
		dependencies.addAll(currentModule.getDependencies());
		currentModule.setDependencies(dependencies);
		// merge properties
		Properties properties = new Properties();
		properties.putAll(parentModule.getProperties());
		properties.putAll(currentModule.getProperties());
		currentModule.setProperties(properties);
		// merge lifecycles
		Map<String, ILifecycleReference> lifecycles = new HashMap<String, ILifecycleReference>();
		lifecycles.putAll(parentModule.getLifecycleDeclarations());
		lifecycles.putAll(currentModule.getLifecycleDeclarations());
		currentModule.setLifecycleDeclarations(lifecycles);
	}

	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);
		if (module == null) {

			// initial module resolve
			for (IRepository repository : config.getWorkspaceRepositories()) {

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
				IRepository localRepo = config.getLocalCacheRepository();
				module = localRepo.materialiseModel(identifier);
			}

			/**
			 * @TODO check in remote repositories too here
			 */

			if (module == null) {
				throw new RhenaException(identifier.toString() + " not found");
			}

			cache.addModule(identifier, module);
			config.getLogger().info(getClass(), identifier, "Materialised model");
		}
		return module;
	}
}
