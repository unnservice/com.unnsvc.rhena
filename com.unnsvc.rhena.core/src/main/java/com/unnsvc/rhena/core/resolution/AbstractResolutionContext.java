
package com.unnsvc.rhena.core.resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.logging.IRhenaLogger;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.logging.RhenaLogger;

/**
 * @TODO maybe add some sort of Class to return from the materialise() methods,
 *       which encapsulates the result and has a status, so we know whether the
 *       repository has failed or wants to signal some error state?
 * @author noname
 *
 */
public abstract class AbstractResolutionContext implements IRhenaContext {

	private Map<String, IRhenaLogger> loggers = new HashMap<String, IRhenaLogger>();
	protected List<IRepository> repositories;
	private Map<Class<? extends IContextEvent>, Set<IContextListener<? extends IContextEvent>>> listeners;

	public AbstractResolutionContext() {

		this.repositories = new ArrayList<IRepository>();
		this.listeners = new HashMap<Class<? extends IContextEvent>, Set<IContextListener<? extends IContextEvent>>>();
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
	public IRhenaExecution materialiseExecution(IRhenaModule module, EExecutionType type) throws RhenaException {

		IRhenaExecution execution = module.getRepository().materialiseExecution(module, type);

		return execution;
	}

	@Override
	public List<IRepository> getRepositories() {

		return repositories;
	}

	@Override
	public void addListener(IContextListener<? extends IContextEvent> listener) {

		Set<IContextListener<? extends IContextEvent>> l = listeners.get(listener.getType());

		if (l == null) {
			l = new HashSet<IContextListener<? extends IContextEvent>>();
			l.add(listener);
			listeners.put(listener.getType(), l);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fireEvent(IContextEvent event) throws RhenaException {

		Set<IContextListener<? extends IContextEvent>> l = listeners.get(event.getClass());
		if (l != null) {
			for (IContextListener listener : l) {

				listener.onEvent(event);
			}
		}
	}

	@Override
	public IRhenaLogger getLogger(Class<?> type) {

		if (!loggers.containsKey(type.getName())) {
			IRhenaLogger logger = new RhenaLogger(this, type.getName());
			loggers.put(type.getName(), logger);
		}
		return loggers.get(type.getName());
	}
}
