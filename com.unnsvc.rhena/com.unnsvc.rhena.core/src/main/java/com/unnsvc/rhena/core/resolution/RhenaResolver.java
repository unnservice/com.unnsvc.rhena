
package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaModule;

public class RhenaResolver implements IResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;
	private Map<ModuleIdentifier, RhenaModel> models;
	private Map<ModuleIdentifier, RhenaModule> modules;

	public RhenaResolver(IRepository... repositories) {

		this.repositories = repositories;
		this.models = new HashMap<ModuleIdentifier, RhenaModel>();
		this.modules = new HashMap<ModuleIdentifier, RhenaModule>();
	}

	@Override
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		RhenaModel model = models.get(moduleIdentifier);
		if (model == null) {
			
			for (IRepository repository : repositories) {

				try {
					model = repository.materialiseModel(moduleIdentifier);
					models.put(moduleIdentifier, model);
					return model;
				} catch (RhenaException repositoryException) {
					log.debug(repositoryException.getMessage(), repositoryException);
				}
			}

			throw new RhenaException("Failed to resolve model of: " + moduleIdentifier + ":model");
		}
		return model;
	}

	@Override
	public RhenaModule materialiseModule(RhenaModel model) {

		RhenaModule module = modules.get(model.getModuleIdentifier());
		if (module == null) {
			module = model.getRepository().materialiseModule(model);
			modules.put(model.getModuleIdentifier(), module);
		}
		return module;
	}
}
