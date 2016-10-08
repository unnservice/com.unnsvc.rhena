package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultResourcesLifecycle implements IResourcesLifecycle {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void compileResources(RhenaModule model) {

		log.info("[" + model.getModuleIdentifier().toString() + "] Compiling resources");
	}

}
