package com.unnsvc.rhena.common;

import java.io.Serializable;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;

public interface ILifecycleBuilder extends Serializable {

	public ILifecycle buildLifecycle(IEntryPoint entryPoint, String lifecycleName) throws RhenaException;

}
