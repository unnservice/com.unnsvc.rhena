
package com.unnsvc.rhena.common;

import java.util.Map;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void addModule(ModuleIdentifier identifier, IRhenaModule module);

	public Map<ModuleIdentifier, IRhenaModule> getModules();

	public Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> getExecutions();

	public Map<EExecutionType, IRhenaExecution> getExecution(ModuleIdentifier target);
}
