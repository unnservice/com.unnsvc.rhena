
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelVisitor {

	public void startModule(IRhenaModule model) throws RhenaException;

	public void endModule(IRhenaModule model) throws RhenaException;
}
