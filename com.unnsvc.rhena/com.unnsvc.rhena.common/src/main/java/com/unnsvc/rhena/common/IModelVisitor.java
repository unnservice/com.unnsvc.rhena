
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModule;

public interface IModelVisitor {

	public void startModel(RhenaModule model) throws RhenaException;

	public void endModel(RhenaModule model) throws RhenaException;

}
