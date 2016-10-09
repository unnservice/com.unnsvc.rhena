
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.RhenaModel;

public interface IModelVisitor {

	public void startModel(RhenaModel model) throws RhenaException;

	public void endModel(RhenaModel model) throws RhenaException;

}
