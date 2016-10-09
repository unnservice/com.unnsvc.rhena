package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaModule;

public class ResolutionManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;
	private Map<ModuleIdentifier, RhenaModule> resolvedModules;

	public ResolutionManager(IRepository... repositories) {

		this.repositories = repositories;
		this.resolvedModules = new HashMap<ModuleIdentifier, RhenaModule>();
	}

	public RhenaModule materialiseState(ModuleIdentifier moduleIdentifier, ModuleState moduleState) throws RhenaException {

		if(resolvedModules.containsKey(moduleIdentifier) && resolvedModules.get(moduleIdentifier).getModuleState().compareTo(moduleState) >= 0) {
			return resolvedModules.get(moduleIdentifier);
		}
		
		for (IRepository repository : repositories) {

			try {

				return repository.materialiseState(moduleIdentifier, moduleState);
			} catch (RhenaException repositoryException) {
				log.debug(repositoryException.getMessage(), repositoryException);
			}
		}
		
		throw new RhenaException("Failed to resolve: " + moduleIdentifier + ":model");
	}

}
