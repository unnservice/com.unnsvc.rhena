
package com.unnsvc.rhena.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;

public class DefaultProcessor implements IProcessor {

	@Override
	public void configure(Document configuration, ExecutionType type) {

	}

	@Override
	public void process(IExecutionContext configurator, IRhenaModule model) {

	}

}
