
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(IExecutionContext context, IRhenaModule module, IExecutionType type) throws RhenaException;

}
