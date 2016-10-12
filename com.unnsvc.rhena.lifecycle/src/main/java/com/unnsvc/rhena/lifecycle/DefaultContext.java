
package com.unnsvc.rhena.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;

public class DefaultContext implements IExecutionContext {

	// // inject these later so we don't get constructor injection complexities
	// later
	// @ExecutionContext
	// private IRhenaModule module;
	// @ExecutionContext
	// private ExecutionType type;
	// @ExecutionContext // this will require more complex checks on lifecycle
	// phase to know whether the configurator is ready and has executed...
	// private Document configuration
	
	private Logger log  = LoggerFactory.getLogger(getClass());

	public DefaultContext() {

	}

	/**
	 * @param type
	 * @TODO the idea is to part some sort of configuration and if values are
	 *       not set then use framework defaults
	 */
	@Override
	public void configure(Document configuration, ExecutionType type) {

	}

}
