
package com.unnsvc.rhena.common;

import java.util.Set;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * This context will perform caching
 */
public interface IRhenaEngine {

	public IRhenaModule materialiseModel(ICaller caller) throws RhenaException;

	/**
	 * We require a processed model to produce an execution.
	 * 
	 * @param entryPointModule
	 * @param prototype
	 * @return
	 * @throws RhenaException
	 */
	public IRhenaExecution materialiseExecution(ICaller caller) throws RhenaException;

	public IRhenaContext getContext();

	public Set<ModuleIdentifier> findRoots(ModuleIdentifier identifier, EExecutionType type);

}
