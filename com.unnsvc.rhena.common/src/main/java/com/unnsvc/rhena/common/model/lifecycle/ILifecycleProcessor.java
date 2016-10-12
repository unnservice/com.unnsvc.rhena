package com.unnsvc.rhena.common.model.lifecycle;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ExecutionType;

public interface ILifecycleProcessor {

	public void configure(Document configuration, ExecutionType type);
}
