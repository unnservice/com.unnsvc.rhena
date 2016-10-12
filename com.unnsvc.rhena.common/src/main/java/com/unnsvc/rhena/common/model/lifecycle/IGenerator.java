package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(IProjectConfiguration configurator, IRhenaModule model);

}
