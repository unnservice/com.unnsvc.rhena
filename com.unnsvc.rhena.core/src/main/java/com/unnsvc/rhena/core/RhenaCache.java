
package com.unnsvc.rhena.core;

import java.util.Map;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class RhenaCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> cachedModules;

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return cachedModules.get(identifier);
	}

	@Override
	public void cacheModule(IRhenaModule module) throws RhenaException {

		this.cachedModules.put(module.getIdentifier(), module);
	}
}
