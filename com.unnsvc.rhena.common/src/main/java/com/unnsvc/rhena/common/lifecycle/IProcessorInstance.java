package com.unnsvc.rhena.common.lifecycle;

import java.io.Serializable;

import com.unnsvc.rhena.common.traversal.IDependencies;

public interface IProcessorInstance extends Serializable {

	public IDependencies getDependencies();

	public String getClassName();

}
