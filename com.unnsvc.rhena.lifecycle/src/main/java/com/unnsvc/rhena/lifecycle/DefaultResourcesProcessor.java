
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultResourcesProcessor implements IResourcesProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModule model;
	private ExecutionType type;

	public DefaultResourcesProcessor(RhenaModule model, ExecutionType type) {

		this.model = model;
		this.type = type;
	}

	@Override
	public void processResources(RhenaModule model) {

		log.info("Processing resources");
	}

}
