
package com.unnsvc.rhena.common;

import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void addModule(ModuleIdentifier identifier, IRhenaModule module) throws RhenaException;

	public Map<ModuleIdentifier, IRhenaModule> getModules();

	public Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> getExecutions();

	public Map<EExecutionType, IRhenaExecution> getExecution(ModuleIdentifier target);

	public boolean containsExecution(ModuleIdentifier moduleIdentifier, EExecutionType eExecutionType);

	public Map<ModuleIdentifier, ILifecycle> getLifecycles();

	public ILifecycle getLifecycle(ModuleIdentifier identifier);

	public void addEdge(IRhenaEdge edge);

	public Set<IRhenaEdge> getEdges();
}
