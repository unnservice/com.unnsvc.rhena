
package com.unnsvc.rhena.common.model.lifecycle;

import java.io.File;
import java.util.List;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;

public interface IExecutionContext extends ILifecycleProcessor {

	public List<File> selectResources(EExecutionType type, String pattern) throws RhenaException;

	public List<IResource> getResources();

	public File getOutputDirectory(IRhenaModule module);

}
