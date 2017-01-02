
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IDependencies;

public interface ILifecycle {

	public IExecutionContext getContext();

	public List<IProcessor> getProcessors();

	public IGenerator getGenerator();

	public File executeLifecycle(IRhenaModule module, EExecutionType executionType, IDependencies dependencies) throws RhenaException;

}
