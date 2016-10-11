
package com.unnsvc.rhena.common;

import java.util.List;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecution;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

/**
 * @TODO Resolver shouldn't be resolution context, which it is now with
 *       RhenaResolutionContext, resolution context needs to be separate?
 * 
 * @author noname
 *
 */
public interface IResolutionContext {

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaExecution materialiseExecution(RhenaModule model, ExecutionType type) throws RhenaException;

	public List<IRepository> getRepositories();
}
