package com.unnsvc.rhena.builder.visitors;

import com.unnsvc.rhena.builder.exceptions.RhenaException;

public interface IVisitable {

	public void visit(IVisitor visitor) throws RhenaException;

}
