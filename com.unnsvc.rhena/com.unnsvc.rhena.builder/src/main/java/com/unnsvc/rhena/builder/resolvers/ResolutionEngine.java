
package com.unnsvc.rhena.builder.resolvers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;

public class ResolutionEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;
	private Map<ModuleIdentifier, RhenaModule> resolutionCache;

	public ResolutionEngine(IRepository... repositories) {

		this.repositories = repositories;
		this.resolutionCache = new HashMap<ModuleIdentifier, RhenaModule>();
	}

	public RhenaModule resolveModule(RhenaContext context, ModuleIdentifier moduleIdentifier) throws RhenaException {

		if(resolutionCache.containsKey(moduleIdentifier)) {
			log.info("Using cached: " + moduleIdentifier);
			return resolutionCache.get(moduleIdentifier);
		}
		
		for (IRepository repository : repositories) {

			try {

				log.info("Resolving: " + moduleIdentifier.toString());
				RhenaModule module = repository.resolveModule(context, moduleIdentifier);
				resolutionCache.put(moduleIdentifier, module);
				return module;
			} catch (RhenaException repositoryException) {
				log.debug(repositoryException.getMessage(), repositoryException);
			}
		}

		throw new RhenaException("Unable to resolve: " + moduleIdentifier.toString());
	}
}
