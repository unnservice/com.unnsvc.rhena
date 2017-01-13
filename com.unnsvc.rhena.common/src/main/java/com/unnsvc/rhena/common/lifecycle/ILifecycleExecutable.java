
package com.unnsvc.rhena.common.lifecycle;

import java.io.Serializable;
import java.util.List;

public interface ILifecycleExecutable extends Serializable {

	public String getLifecycleName();

	public ILifecycleProcessorExecutable getContextExecutable();

	public List<ILifecycleProcessorExecutable> getProcessorExecutables();

	public ILifecycleProcessorExecutable getGeneratorExecutable();

	public List<ICustomLifecycleCommandExecutable> getCommandExecutables();
}
