
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaModule;

public class RemoteRepository implements IRepository {

	@Override
	public RhenaModel materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public RhenaModule materialiseModule(RhenaModel model) {

		throw new UnsupportedOperationException("Not implemented");

	}

}
