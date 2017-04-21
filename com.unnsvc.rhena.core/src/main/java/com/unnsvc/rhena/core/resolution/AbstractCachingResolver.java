
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.traversal.AbstractFlatTreeWalker;

public abstract class AbstractCachingResolver extends AbstractFlatTreeWalker {

	private IRhenaContext context;

	public AbstractCachingResolver(IRhenaContext context) {

		super();
		this.context = context;
	}

	/**
	 * All resolutions go through this because we perform caching
	 * 
	 * @param identifier
	 * @return
	 * @throws RhenaException
	 */
	@Override
	protected IRhenaModule onResolveModule(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = context.getCache().getModule(identifier);
		if (module == null) {
			module = getContext().getResolver().resolveModule(identifier);
			getContext().getCache().cacheModule(module);
		}
		return module;
	}

	public IRhenaContext getContext() {

		return context;
	}
}
