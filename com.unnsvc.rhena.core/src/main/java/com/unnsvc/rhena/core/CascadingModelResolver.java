
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.execution.UniqueStack;

/**
 * This class will also check whether there are any cyclic relationships
 * 
 * @author noname
 *
 */
public class CascadingModelResolver implements IModelResolver {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, IRhenaModule> modules;
	private Set<ModuleIdentifier> merged;

	public CascadingModelResolver(IRhenaConfiguration config) {

		this.config = config;
		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
		this.merged = new HashSet<ModuleIdentifier>();
	}

	/**
	 * Cascading resolver which performs cyclic checking.
	 * 
	 * @TODO make bolognese out of this pasta
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
						ILifecycleDeclaration lifecycle = currentModule.getLifecycleDeclarations().get(currentModule.getLifecycleName());
						if (lifecycle == null) {
							throw new RhenaException("Could not find lifecycle " + currentModule.getLifecycleName() + " in " + currentModule.getIdentifier());
						}
						if (!processed.contains(lifecycle.getContext().getModuleEdge().getEntryPoint())) {
							tracker.pushUnique(lifecycle.getContext().getModuleEdge().getEntryPoint());
							break edgeProcessing;
						}
						for (IProcessorReference processor : lifecycle.getProcessors()) {
							if (!processed.contains(processor.getModuleEdge().getEntryPoint())) {
								tracker.pushUnique(processor.getModuleEdge().getEntryPoint());
								break edgeProcessing;
							}
						}
						if (!processed.contains(lifecycle.getGenerator().getModuleEdge().getEntryPoint())) {
							tracker.pushUnique(lifecycle.getGenerator().getModuleEdge().getEntryPoint());
							break edgeProcessing;
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
		} catch (NotUniqueException nue) {
			System.err.println(getClass().getName() + " Cyclic dependency path detected:");
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
					System.err.println(getClass() + " - cycle: " + (shift ? "↓" : "↓") + " "
							+ materialiseModel(edge.getTarget()).getIdentifier().toTag(edge.getExecutionType()));
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
		Map<String, ILifecycleDeclaration> lifecycles = new HashMap<String, ILifecycleDeclaration>();
		lifecycles.putAll(parentModule.getLifecycleDeclarations());
		lifecycles.putAll(currentModule.getLifecycleDeclarations());
		currentModule.setLifecycleDeclarations(lifecycles);
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

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

	@Override
	public Map<ModuleIdentifier, IRhenaModule> getModules() {

		return modules;
	}
}
