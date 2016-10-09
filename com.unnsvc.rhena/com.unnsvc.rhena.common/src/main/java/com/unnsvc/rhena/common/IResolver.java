package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaExecution;

public interface IResolver {


	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public RhenaExecution materialiseModuleType(RhenaModel model, RhenaExecutionType type) throws RhenaException;

}
