
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.EExecutionType;
import com.unnsvc.rhena.common.ng.model.ESelectionType;
import com.unnsvc.rhena.common.ng.model.IEntryPoint;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.core.treewalk.AbstractFlatTreeWalker;
import com.unnsvc.rhena.model.EntryPoint;

public class CascadingModelResolver extends AbstractFlatTreeWalker {

	private IRhenaConfiguration config;
	private IRhenaResolver resolver;
	private IRhenaCache cache;

	public CascadingModelResolver(IRhenaConfiguration config, IRhenaResolver resolver, IRhenaCache cache) {

		super();
		this.config = config;
		this.resolver = resolver;
		this.cache = cache;
	}

	public IRhenaModule resolveModuleTree(ModuleIdentifier identifier) throws RhenaException {

		IEntryPoint entryPoint = new EntryPoint(EExecutionType.TEST, identifier);

		IRhenaModule module = visitTree(entryPoint, ESelectionType.SCOPE);

		return module;
	}

	/**
	 * All resolutions go through this because we perform caching
	 * 
	 * @param identifier
	 * @return
	 * @throws RhenaException
	 */
	@Override
	protected IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);
		if (module == null) {
			module = resolver.resolveModule(identifier);
			cache.cacheModule(module);
		}
		return module;
	}
}
