package com.unnsvc.rhena.common.lifecycle;

import com.unnsvc.rhena.common.traversal.IDependencies;

public interface IProcessorInstance {

	public IDependencies getDependencies();

	public String getClassName();

}
