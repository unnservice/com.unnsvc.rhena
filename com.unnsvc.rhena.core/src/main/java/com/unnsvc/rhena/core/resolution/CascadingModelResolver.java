
package com.unnsvc.rhena.core.resolution;

import java.util.Stack;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.IRhenaResolver;
import com.unnsvc.rhena.model.RhenaMergedModule;

public class CascadingModelResolver {

	private IRhenaConfiguration config;
	private IRhenaResolver resolver;
	private IRhenaCache cache;

	public CascadingModelResolver(IRhenaConfiguration config, IRhenaResolver resolver, IRhenaCache cache) {

		this.config = config;
		this.resolver = resolver;
		this.cache = cache;
	}

	public IRhenaModule resolveModule(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule mergedModule = resolveMergedModule(identifier);

		return mergedModule;
	}

	/**
	 * This method resolves a merged module that has all of its parents merged
	 */
	private IRhenaModule resolveMergedModule(ModuleIdentifier identifier) throws RhenaException {

		/**
		 * First element is the child, second the parent, third the parent of
		 * parent, ...
		 */
		Stack<IRhenaModule> parentStack = new Stack<IRhenaModule>();

		IRhenaModule currentModule = _resolveModule(identifier);
		parentStack.push(currentModule);

		while (currentModule.getParent() != null) {

			currentModule = _resolveModule(currentModule.getParent().getEntryPoint().getTarget());
			parentStack.push(currentModule);
		}

		RhenaMergedModule mergedModule = null;
		// we start with the greatest parent, and merge each module into that
		for (int i = parentStack.size() - 1; i >= 0; i--) {
			mergedModule = mergeInto(parentStack.get(i), mergedModule);
		}

		return mergedModule;
	}

	private RhenaMergedModule mergeInto(IRhenaModule from, RhenaMergedModule into) {

		if (into == null) {
			return new RhenaMergedModule(from);
		}

		into.mergeOverwrite(from);
		return into;
	}

	/**
	 * All resolutions go through this because we perform caching
	 * 
	 * @param identifier
	 * @return
	 * @throws RhenaException
	 */
	private IRhenaModule _resolveModule(ModuleIdentifier identifier) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);
		if (module == null) {
			module = resolver.resolveModule(identifier);
			cache.cacheModule(module);
		}
		return module;
	}

}
