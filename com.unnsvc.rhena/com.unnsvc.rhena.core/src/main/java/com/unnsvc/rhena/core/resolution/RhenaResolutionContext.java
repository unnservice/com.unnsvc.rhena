
package com.unnsvc.rhena.core.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public class RhenaResolutionContext implements IResolutionContext {

	private Logger log = LoggerFactory.getLogger(getClass());
	private List<IRepository> repositories;
	private Map<ModuleIdentifier, RhenaModel> models;
	private Map<ModuleIdentifier, Map<RhenaExecutionType, RhenaExecution>> executions;

	public RhenaResolutionContext() {

		this.repositories = new ArrayList<IRepository>();
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
	public RhenaExecution materialiseExecution(RhenaModel model, RhenaExecutionType type) throws RhenaException {

		/**
		 * Execution type might have a dependency on another execution type
		 * first which needs to be executed
		 */
		if (type.getDependency() != null) {
			materialiseExecution(model, type.getDependency());
		}

		ModuleIdentifier identifier = model.getModuleIdentifier();

		if (executions.get(identifier) != null && executions.get(identifier).get(type) != null) {
			return executions.get(identifier).get(type);
		}

		// else materialise it

		/**
		 * Doing this one-off thing of passing down this context into the
		 * repository, so the repository can use the context if it needs
		 */
		RhenaExecution execution = model.getRepository().materialiseExecution(model, type);

		if (executions.containsKey(identifier)) {
			
			executions.get(identifier).put(type, execution);
		} else {
			Map<RhenaExecutionType, RhenaExecution> typeExecutions = new HashMap<RhenaExecutionType, RhenaExecution>();
			typeExecutions.put(type, execution);
			executions.put(identifier, typeExecutions);
		}
		
		log.info("[" + identifier + "]:" + type.toLabel() + " materialised");


		// RhenaModule module = modules.get(new Object[] {
		// model.getModuleIdentifier(), type });
		// if (module == null) {
		// module = model.getRepository().materialiseModule(model, type);
		// modules.put(model.getModuleIdentifier(), module);
		// }
		return execution;
	}

	@Override
	public List<IRepository> getRepositories() {

		return repositories;
	}
}
