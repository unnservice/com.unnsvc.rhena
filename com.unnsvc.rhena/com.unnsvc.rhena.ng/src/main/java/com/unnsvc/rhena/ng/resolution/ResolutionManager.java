package com.unnsvc.rhena.ng.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.model.RhenaModule;

public class ResolutionManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;

	public ResolutionManager(IRepository... repositories) {

		this.repositories = repositories;
	}

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		for (IRepository repository : repositories) {

			try {

				return repository.materialiseModel(moduleIdentifier);
			} catch (RhenaException repositoryException) {
				log.debug(repositoryException.getMessage(), repositoryException);
			}
		}
		
		throw new RhenaException("Failed to resolve: " + moduleIdentifier + ":model");
	}

}
