
package com.unnsvc.rhena.common;

import java.util.List;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

/**
 * @TODO Resolver shouldn't be resolution context, which it is now with
 *       RhenaResolutionContext, resolution context needs to be separate?
 * 
 * @author noname
 *
 */
public interface IResolutionContext {

	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRhenaExecution materialiseExecution(IRhenaModule model, IExecutionType type) throws RhenaException;

	public List<IRepository> getRepositories();
}
