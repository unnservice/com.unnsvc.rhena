
package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class RhenaReference extends RhenaModule {

	public RhenaReference(ModuleIdentifier moduleIdentifier) {
		
		super(moduleIdentifier, null);
	}

	@Override
	public void visit(IModelVisitor visitor) throws RhenaException {

		throw new UnsupportedOperationException("Cannot visit an unresolved reference");
	}
}
