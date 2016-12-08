
package com.unnsvc.rhena.core.visitors;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

/**
 * This depchain visitor visits the model procedurally instead of recursively
 * 
 * @author noname
 *
 */
public abstract class AbstractProceduralModelVisitor implements IModelVisitor {

	private IModelResolver resolver;

	public AbstractProceduralModelVisitor(IModelResolver resolver) {

		this.resolver = resolver;
	}

	@Override
	public void visit(IRhenaModule module) throws RhenaException {

		System.err.println(module.getIdentifier());
	}

	public abstract void startVisit(IRhenaModule module);

	public abstract void endVisit(IRhenaModule module);
}
