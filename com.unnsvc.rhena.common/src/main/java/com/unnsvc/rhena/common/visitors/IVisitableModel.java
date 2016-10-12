
package com.unnsvc.rhena.common.visitors;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IVisitableModel {

	public <T extends IModelVisitor> T visit(T visitor) throws RhenaException;

}
