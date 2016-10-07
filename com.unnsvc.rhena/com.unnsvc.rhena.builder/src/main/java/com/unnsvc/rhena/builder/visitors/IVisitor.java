
package com.unnsvc.rhena.builder.visitors;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.model.RhenaModuleDescriptor;

public interface IVisitor {

	public void startVisit(RhenaModuleDescriptor rhenaModuleDescriptor) throws RhenaException;

	public void endVisit(RhenaModuleDescriptor rhenaModuleDescriptor) throws RhenaException;

}
