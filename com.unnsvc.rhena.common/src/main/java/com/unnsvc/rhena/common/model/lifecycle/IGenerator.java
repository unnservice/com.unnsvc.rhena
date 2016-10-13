
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(IExecutionContext context, IRhenaModule module, ExecutionType type) throws RhenaException;

}
