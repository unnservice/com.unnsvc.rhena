
package com.unnsvc.rhena.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.annotation.ExecutionContext;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IConfigurator;

public class DefaultConfigurator implements IConfigurator {

	@ExecutionContext
	private IRhenaModule module;
	@ExecutionContext
	private ExecutionType type;

	public DefaultConfigurator() {

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
