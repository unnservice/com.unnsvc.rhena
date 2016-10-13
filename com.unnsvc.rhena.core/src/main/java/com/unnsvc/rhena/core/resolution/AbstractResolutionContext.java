
package com.unnsvc.rhena.core.resolution;

import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.ExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @TODO maybe add some sort of Class to return from the materialise() methods,
 *       which encapsulates the result and has a status, so we know whether the
 *       repository has failed or wants to signal some error state?
 * @author noname
 *
 */
public abstract class AbstractResolutionContext implements IResolutionContext {

	// private Logger log = LoggerFactory.getLogger(getClass());
	protected List<IRepository> repositories;

	public AbstractResolutionContext() {

		this.repositories = new ArrayList<IRepository>();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		IRhenaModule module = null;

		for (IRepository repository : repositories) {

			module = repository.materialiseModel(moduleIdentifier);

			if (module != null) {

				return module;
			}

		}

		return module;
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule module, ExecutionType type) throws RhenaException {

		IRhenaExecution execution = module.getRepository().materialiseExecution(module, type);

		return execution;
	}

	@Override
	public List<IRepository> getRepositories() {

		return repositories;
	}
}
