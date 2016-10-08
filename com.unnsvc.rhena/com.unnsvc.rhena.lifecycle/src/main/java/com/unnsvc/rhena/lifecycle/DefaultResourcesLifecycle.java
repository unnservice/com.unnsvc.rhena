
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultResourcesLifecycle implements IResourcesLifecycle {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModule model;
	private CompositeScope scope;
	
	public DefaultResourcesLifecycle() {
		
	}
	
	public DefaultResourcesLifecycle(RhenaModule model, CompositeScope scope) {
		
		this.model = model;
		this.scope = scope;
	}

	@Override
	public void compileResources() {

//		log.info("[" + model.getModuleIdentifier().toString() + "] Compiling resources");
	}

	@Override
	public IResourcesLifecycle newDefaultResourcesLifecycle(RhenaModule model, CompositeScope scope) {

		return new DefaultResourcesLifecycle(model, scope);
	}
}
