
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.model.IRhenaModule;

public class LoadedModule {

	private IRhenaModule module;

	public LoadedModule(IRhenaModule module) {

		this.module = module;
	}

	public IRhenaModule getModule() {

		return module;
	}
}
