
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * This context will perform caching
 */
public interface IRhenaEngine {

	public IRhenaModule materialiseModel(ModuleIdentifier valueOf) throws RhenaException;

	/**
	 * We require a processed model to produce an execution.
	 * 
	 * @param entryPointModule
	 * @param prototype
	 * @return
	 * @throws RhenaException
	 */
	public IRhenaExecution materialiseExecution(IRhenaModule entryPointModule, EExecutionType prototype) throws RhenaException;

	public IRhenaContext getContext();

	public IRhenaCache getCache();

}
