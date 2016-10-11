
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IRepository {
	
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaExecution materialiseExecution(RhenaModule model, ExecutionType type) throws RhenaException;
	
}
