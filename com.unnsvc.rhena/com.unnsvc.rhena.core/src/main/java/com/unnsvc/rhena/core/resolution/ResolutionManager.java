package com.unnsvc.rhena.core.resolution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModule;

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
