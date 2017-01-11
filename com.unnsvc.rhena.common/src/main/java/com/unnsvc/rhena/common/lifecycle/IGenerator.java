
package com.unnsvc.rhena.common.lifecycle;

import java.io.File;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IGenerator extends ILifecycleProcessor {

	public File generate(IRhenaModule module, EExecutionType type) throws RhenaException;

}
