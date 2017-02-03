
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.exceptions.NotUniqueException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.UniqueStack;

/**
 * This class will also check whether there are any cyclic relationships
 * 
 * @author noname
 *
 */
public class CascadingModelResolver implements IModelResolver {

	private IRhenaContext context;
	private IRhenaCache cache;

	public CascadingModelResolver(IRhenaContext context, IRhenaCache cache) {

		this.context = context;
		this.cache = cache;
	}

	/**
	 * Cascading resolver which performs cyclic checking.
	 * 
	 * @TODO make bolognese out of this pasta, factor out the break
	 * @param entryPoint
	 * @throws RhenaException
	 */
	@Override
	public IRhenaModule resolveEntryPoint(IEntryPoint entryPoint) throws RhenaException {

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
					 * Resolve lifecycle from here after we've merged parents
					 */
					if (!currentModule.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
						ILifecycleReference lifecycle = currentModule.getMergedLifecycleDeclarations(cache).get(currentModule.getLifecycleName());
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
					for (IRhenaEdge dependency : currentModule.getMergedDependencies(cache)) {

						/**
						 * We only care about dependencies which we can use in
						 * the requested scope
						 */
						if (currentEntryPoint.getExecutionType().compareTo(dependency.getEntryPoint().getExecutionType()) >= 0) {
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
			context.getLogger().error(getClass(), entryPoint.getTarget(), "Cyclic dependency path detected:");
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
					context.getLogger().error(getClass(), entryPoint.getTarget(),
							"Cycle: " + (shift ? "↓" : "↓") + " " + materialiseModel(edge.getTarget()).getIdentifier().toTag(edge.getExecutionType()));
					shift = !shift;
				}
			}
			throw new RhenaException(nue.getMessage(), nue);
		}

		return materialiseModel(entryPoint.getTarget());
	}

	protected IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);
		if (module == null) {

			// initial module resolve
			for (IRepository repository : context.getWorkspaceRepositories()) {

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
				IRepository localRepo = context.getLocalCacheRepository();
				module = localRepo.materialiseModel(identifier);
			}

			if (module == null) {
				for (IRepository additionalRepo : context.getAdditionalRepositories()) {
					module = additionalRepo.materialiseModel(identifier);
					if (module != null) {
						break;
					}
				}
			}

			/**
			 * @TODO check in remote repositories too here
			 */

			if (module == null) {
				throw new RhenaException(identifier.toString() + " not found");
			}

			cache.addModule(identifier, module);
			context.getLogger().info(getClass(), identifier, "Materialised model");
		}
		return module;
	}
}
