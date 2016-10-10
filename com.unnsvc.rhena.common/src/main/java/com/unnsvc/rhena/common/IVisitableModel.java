
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IVisitableModel {

	public void visit(IModelVisitor visitor) throws RhenaException;

}
