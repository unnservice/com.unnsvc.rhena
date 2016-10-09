
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdgeType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaExecution;

public interface IRepository {
	
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaExecution materialiseModule(RhenaModel model, RhenaEdgeType type);
	
}
