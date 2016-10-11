
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class DefaultResourcesProcessor implements IResourcesProcessor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaModule model;
	private ExecutionType type;

	public DefaultResourcesProcessor(IRhenaModule model, ExecutionType type) {

		this.model = model;
		this.type = type;
	}

	@Override
	public void processResources(IRhenaModule model) {

		log.info("Processing resources");
	}

}
