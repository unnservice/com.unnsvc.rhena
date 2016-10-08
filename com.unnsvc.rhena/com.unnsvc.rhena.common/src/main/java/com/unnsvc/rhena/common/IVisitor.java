
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IVisitor {

	public void startModule(RhenaModule module) throws RhenaException;

	public void endModule(RhenaModule module) throws RhenaException;

}
