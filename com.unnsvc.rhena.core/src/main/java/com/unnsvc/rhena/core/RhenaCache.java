package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.model.RhenaMergedModule;

public class RhenaCache implements IRhenaCache {

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void cacheModule(IRhenaModule module) throws RhenaException {

		if(module instanceof RhenaMergedModule) {
			throw new RhenaException("Cannot cache merged modules");
		}
		
		throw new UnsupportedOperationException("Not implemented");
	}

}
