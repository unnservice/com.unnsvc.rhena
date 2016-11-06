
package com.unnsvc.rhena.common;

import java.net.URI;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRepository {
	
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRhenaExecution materialiseExecution(IRhenaContext context, IRhenaModule module, EExecutionType type) throws RhenaException;
	
	public URI getLocation();
	
}
