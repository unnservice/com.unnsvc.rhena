
package com.unnsvc.rhena.core.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;

public class ProcessorReference implements IProcessorReference {

	private IRhenaModule module;
	private String clazz;
	private Document configuration;

	public ProcessorReference(IRhenaModule module, String clazz, Document configuration) {

		this.module = module;
		this.clazz = clazz;
		this.configuration = configuration;
	}

	@Override
	public IRhenaModule getModule() {

		return module;
	}

	@Override
	public void setModule(IRhenaModule module) {

		this.module = module;
	}

	@Override
	public String getClazz() {

		return clazz;
	}

	@Override
	public Document getConfiguration() {

		return configuration;
	}
}
