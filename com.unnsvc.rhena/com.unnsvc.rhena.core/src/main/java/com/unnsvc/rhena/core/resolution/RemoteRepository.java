
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;

public class RemoteRepository implements IRepository {

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) {

		throw new UnsupportedOperationException("Not implemented");

	}

	public RhenaLifecycleExecution materialisePackaged(RhenaModule module) throws RhenaException {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RhenaModule materialiseState(ModuleIdentifier moduleIdentifier, ModuleState moduleState) throws RhenaException {

		// TODO Auto-generated method stub

		return null;
	}

}
