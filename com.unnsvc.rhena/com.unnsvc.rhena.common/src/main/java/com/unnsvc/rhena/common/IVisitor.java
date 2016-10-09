
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;

public interface IVisitor {

	public void startModule(RhenaModel module) throws RhenaException;

	public void endModule(RhenaModel module) throws RhenaException;

}
