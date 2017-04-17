
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRhenaResolver;
import com.unnsvc.rhena.core.treewalk.AbstractFlatTreeWalker;

public abstract class AbstractCachingResolver extends AbstractFlatTreeWalker {

	private IRhenaResolver resolver;

	private IRhenaCache cache;

	public AbstractCachingResolver(IRhenaCache cache, IRhenaResolver resolver) {

		super();
		this.cache = cache;
		this.resolver = resolver;
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

		IRhenaModule module = cache.getModule(identifier);
		if (module == null) {
			module = resolver.resolveModule(identifier);
			cache.cacheModule(module);
		}
		return module;
	}

	public IRhenaCache getCache() {

		return cache;
	}
}
