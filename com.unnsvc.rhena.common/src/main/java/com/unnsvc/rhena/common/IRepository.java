
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.RhenaExecution;

public interface IRepository {
	
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) throws RhenaException;
	
}
