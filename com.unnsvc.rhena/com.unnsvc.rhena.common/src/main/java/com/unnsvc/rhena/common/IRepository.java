
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IRepository {

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaLifecycleExecution materialisePackaged(RhenaModule module) throws RhenaException;

}
