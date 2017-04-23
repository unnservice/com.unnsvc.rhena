
package com.unnsvc.rhena.common;

import java.util.Set;

import com.unnsvc.rhena.common.exceptions.NotFoundException;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IExecutionResult;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IRhenaCache {

	public IRhenaModule getModule(ModuleIdentifier identifier);

	public void cacheModule(IRhenaModule module) throws RhenaException;

	public void cacheExecution(IEntryPoint entryPoint, IExecutionResult result);

	public IExecutionResult getCachedExecution(IEntryPoint entryPoint) throws NotFoundException;

	public Set<IEntryPoint> getEntryPoints();

	public void cacheEntryPoint(IEntryPoint entryPoint);
}
