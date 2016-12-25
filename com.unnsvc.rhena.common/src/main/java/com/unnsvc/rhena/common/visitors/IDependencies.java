
package com.unnsvc.rhena.common.visitors;

import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;

public interface IDependencies {

	public void addDependency(EExecutionType executionType, IRhenaExecution execution);

	public Map<EExecutionType, List<IRhenaExecution>> getDependencies();

	public String getAsClasspath();

}
