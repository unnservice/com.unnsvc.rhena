
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;

public interface IRecursingVisitor extends IVisitor {

	public void enterParent(ModuleIdentifier parentModule) throws RhenaException;

	public void enterLifecycle(ModuleIdentifier lifecycleDeclaration) throws RhenaException;

}
