
package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public class RhenaResolver implements IResolver {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRepository[] repositories;
	private Map<ModuleIdentifier, RhenaModel> models;
	private Map<ModuleIdentifier, Map<RhenaExecutionType, RhenaExecution>> executions;

	public RhenaResolver(IRepository... repositories) {

		this.repositories = repositories;
		this.models = new HashMap<ModuleIdentifier, RhenaModel>();
		this.executions = new HashMap<ModuleIdentifier, Map<RhenaExecutionType, RhenaExecution>>();
	}

	@Override
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		RhenaModel model = models.get(moduleIdentifier);
		if (model == null) {

			for (IRepository repository : repositories) {

				try {
					model = repository.materialiseModel(moduleIdentifier);
					models.put(moduleIdentifier, model);

					log.info("[" + moduleIdentifier + "]:model materialised");

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
	public RhenaExecution materialiseModuleType(RhenaModel model, RhenaExecutionType type) {

		/**
		 * Execution type might have a dependency on another execution type
		 * first which needs to be executed
		 */
		if (type.getDependency() != null) {
			materialiseModuleType(model, type.getDependency());
		}

		ModuleIdentifier identifier = model.getModuleIdentifier();

		if (executions.get(identifier) != null && executions.get(identifier).get(type) != null) {
			return executions.get(identifier).get(type);
		}

		// else materialise it
		RhenaExecution execution = model.getRepository().materialiseExecution(model, type);

		Map<RhenaExecutionType, RhenaExecution> typeExecutions = new HashMap<RhenaExecutionType, RhenaExecution>();
		typeExecutions.put(type, execution);
		executions.put(identifier, typeExecutions);
		log.info("[" + identifier + "]:" + type.toLabel() + " materialised");

		// RhenaModule module = modules.get(new Object[] {
		// model.getModuleIdentifier(), type });
		// if (module == null) {
		// module = model.getRepository().materialiseModule(model, type);
		// modules.put(model.getModuleIdentifier(), module);
		// }
		return execution;
	}
}
