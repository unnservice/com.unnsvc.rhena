
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.DependencyType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultResourcesLifecycle implements IResourcesLifecycle {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModule model;
	private DependencyType dependencyType;
	
	public DefaultResourcesLifecycle() {
		
	}
	
	public DefaultResourcesLifecycle(RhenaModule model, DependencyType dependencyType) {
		
		this.model = model;
		this.dependencyType = dependencyType;
	}
	
	@Override
	public void compileResources() {

//		log.info("[" + model.getModuleIdentifier().toString() + "] Compiling resources");
	}

	@Override
	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModule model, DependencyType scope) {

		return new DefaultResourcesLifecycle(model, scope);
	}
}
