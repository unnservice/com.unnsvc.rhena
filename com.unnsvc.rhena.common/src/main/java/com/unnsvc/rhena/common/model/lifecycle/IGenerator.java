package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(IRhenaModule model, IProjectConfiguration configurator) throws RhenaException;

}
