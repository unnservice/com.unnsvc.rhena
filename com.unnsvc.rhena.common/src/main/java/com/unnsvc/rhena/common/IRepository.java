
package com.unnsvc.rhena.common;

import java.net.URI;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRepository {

	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRhenaExecution materialiseExecution(IExecutionCache cache, IModelResolver resolver, IEntryPoint entryPoint) throws RhenaException;

	public URI getLocation();
}
