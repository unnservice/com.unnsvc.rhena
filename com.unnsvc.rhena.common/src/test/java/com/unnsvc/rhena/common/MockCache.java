
package com.unnsvc.rhena.common;

import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public class MockCache implements IRhenaCache {

	private Map<ModuleIdentifier, IRhenaModule> modules;

	public MockCache() {

		this.modules = new HashMap<ModuleIdentifier, IRhenaModule>();
	}

	@Override
	public IRhenaModule getModule(ModuleIdentifier identifier) {

		return modules.get(identifier);
	}

	@Override
	public void cacheModule(IRhenaModule module) {

		modules.put(module.getIdentifier(), module);
	}

}
