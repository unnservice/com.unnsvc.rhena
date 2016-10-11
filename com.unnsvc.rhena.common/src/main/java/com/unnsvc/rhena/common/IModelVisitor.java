
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelVisitor {

	public void startModel(IRhenaModule model) throws RhenaException;

	public void endModel(IRhenaModule model) throws RhenaException;
}
