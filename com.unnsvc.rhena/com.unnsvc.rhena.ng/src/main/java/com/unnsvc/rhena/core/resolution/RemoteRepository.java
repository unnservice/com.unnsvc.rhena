
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;

public class RemoteRepository implements IRepository {

	@Override
	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) {

		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public RhenaLifecycleExecution materialiseScope(RhenaModule model, CompositeScope scope) {

		// TODO Auto-generated method stub
		return null;
	}

}
