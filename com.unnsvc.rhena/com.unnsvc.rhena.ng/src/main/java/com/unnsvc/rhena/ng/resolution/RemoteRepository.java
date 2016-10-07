
package com.unnsvc.rhena.ng.resolution;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.RhenaContext;
import com.unnsvc.rhena.ng.model.RhenaModule;

public class RemoteRepository implements IRepository {

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) {

		throw new UnsupportedOperationException("Not implemented");

	}

}
