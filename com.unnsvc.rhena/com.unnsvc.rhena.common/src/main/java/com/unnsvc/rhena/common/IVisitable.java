
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IVisitable {

	public void visit(IVisitor visitor) throws RhenaException;

}
