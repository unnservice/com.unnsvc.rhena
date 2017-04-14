
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.search.AFlatTreeVisitor;

public class CascadingModelResolver extends AFlatTreeVisitor implements IModelResolver {

	private IRhenaContext context;

	public CascadingModelResolver(IRhenaContext context, IRhenaCache cache) {

		super(context.getLogger(), cache);
		this.context = context;
	}

	@Override
	public IRhenaModule resolveEntryPoint(IEntryPoint entryPoint) throws RhenaException {

		return visitTree(entryPoint, ESelectionType.SCOPE);
	}

	@Override
	protected IRhenaModule resolveModel(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = getCache().getModule(identifier);
		if (module == null) {

			// initial module resolve
			for (IRepository repository : getContext().getWorkspaceRepositories()) {

				try {
					module = repository.materialiseModel(identifier);
					if (module != null) {
						break;
					}
				} catch (NotFoundException nee) {
					// no-op
				}
			}

			if (module == null) {
				IRepository localRepo = getContext().getLocalCacheRepository();
				module = localRepo.materialiseModel(identifier);
			}

			if (module == null) {
				for (IRepository additionalRepo : getContext().getAdditionalRepositories()) {
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

			getCache().addModule(identifier, module);
			getContext().getLogger().info(getClass(), identifier, "Materialised model");
		}
		return module;
	}

	protected IRhenaContext getContext() {

		return context;
	}
}
