
package com.unnsvc.rhena.common.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelVisitor {

	public void startModule(IRhenaModule module) throws RhenaException;

	public void endModule(IRhenaModule module) throws RhenaException;
}
