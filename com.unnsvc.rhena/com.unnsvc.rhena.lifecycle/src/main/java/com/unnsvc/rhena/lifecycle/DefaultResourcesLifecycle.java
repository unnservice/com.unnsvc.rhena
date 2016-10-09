
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModel;

public class DefaultResourcesLifecycle implements IResourcesLifecycle {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModel model;
	private RhenaExecutionType dependencyType;
	
	public DefaultResourcesLifecycle() {
		
	}
	
	public DefaultResourcesLifecycle(RhenaModel model, RhenaExecutionType dependencyType) {
		
		this.model = model;
		this.dependencyType = dependencyType;
	}
	
	@Override
	public void compileResources() {

//		log.info("[" + model.getModuleIdentifier().toString() + "] Compiling resources");
	}

	@Override
	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModel model, RhenaExecutionType scope) {

		return new DefaultResourcesLifecycle(model, scope);
	}
}
