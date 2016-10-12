
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RemoteRepository implements IRepository {

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented");

	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaModule model, ExecutionType type) {

		throw new UnsupportedOperationException("Not implemented");

	}

}
