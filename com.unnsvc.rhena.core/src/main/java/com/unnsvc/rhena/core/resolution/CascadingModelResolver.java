
package com.unnsvc.rhena.core.resolution;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.IRhenaCache;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;

public class CascadingModelResolver {

	private IRhenaConfiguration config;
	private IRhenaCache cache;

	public CascadingModelResolver(IRhenaConfiguration config, IRhenaCache cache) {

		this.config = config;
		this.cache = cache;
	}

	public IRhenaModule resolveModule(ModuleIdentifier identifier) {

		
		return null;
	}

	
}
