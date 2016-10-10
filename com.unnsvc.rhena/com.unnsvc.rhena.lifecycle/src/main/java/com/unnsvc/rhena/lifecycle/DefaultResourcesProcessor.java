
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.RhenaExecutionType;
import com.unnsvc.rhena.common.model.RhenaModule;

public class DefaultResourcesProcessor implements IResourcesProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModule model;
	private RhenaExecutionType type;

	public DefaultResourcesProcessor(RhenaModule model, RhenaExecutionType type) {

		this.model = model;
		this.type = type;
	}

	@Override
	public void processResources(RhenaModule model) {

		log.info("Processing resources");
	}

}
