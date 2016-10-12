
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRepository {
	
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) throws RhenaException;
	
}
