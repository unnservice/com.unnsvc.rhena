package com.unnsvc.rhena.common;

import java.util.Map;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelResolver {


	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException;

}
