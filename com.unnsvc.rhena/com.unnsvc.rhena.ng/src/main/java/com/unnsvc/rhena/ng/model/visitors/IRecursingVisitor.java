
package com.unnsvc.rhena.ng.model.visitors;

import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;

public interface IRecursingVisitor extends IVisitor {

	public void enterParent(ModuleIdentifier parentModule) throws RhenaException;

	public void enterLifecycle(ModuleIdentifier lifecycleDeclaration) throws RhenaException;

}
