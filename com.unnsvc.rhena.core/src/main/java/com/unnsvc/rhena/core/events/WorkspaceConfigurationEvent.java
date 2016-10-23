
package com.unnsvc.rhena.core.events;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;

public class WorkspaceConfigurationEvent implements IContextEvent {

	private ModuleIdentifier moduleIdentifier;
	private IExecutionContext executionContext;

	public WorkspaceConfigurationEvent(ModuleIdentifier moduleIdentifier, IExecutionContext executionContext) {

		this.moduleIdentifier = moduleIdentifier;
		this.executionContext = executionContext;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public IExecutionContext getExecutionContext() {

		return executionContext;
	}
}
