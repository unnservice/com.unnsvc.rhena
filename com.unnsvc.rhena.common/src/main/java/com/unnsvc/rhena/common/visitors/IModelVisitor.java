
package com.unnsvc.rhena.common.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IModelVisitor {

	public void visit(IRhenaModule module) throws RhenaException;
}
