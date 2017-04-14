package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public class RhenaCache implements IRhenaCache {

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void cacheModule(IRhenaModule module) {

		throw new UnsupportedOperationException("Not implemented");
	}

}
