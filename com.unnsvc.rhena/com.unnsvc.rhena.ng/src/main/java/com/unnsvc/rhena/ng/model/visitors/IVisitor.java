
package com.unnsvc.rhena.ng.model.visitors;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.ng.model.RhenaModule;

public interface IVisitor {

	public void startModule(RhenaModule module) throws RhenaException;

	public void endModule(RhenaModule module) throws RhenaException;

}
