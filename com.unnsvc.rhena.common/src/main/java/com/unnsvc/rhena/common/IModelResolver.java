package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelResolver {

	public IRhenaModule resolveEntryPoint(IEntryPoint entryPoint) throws RhenaException;

}
