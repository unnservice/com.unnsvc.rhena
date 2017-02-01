
package com.unnsvc.rhena.common.search;

import java.io.Serializable;
import java.net.URL;
import java.util.List;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;

public interface IDependencies extends Serializable {

	public List<IRhenaExecution> getDependencies(EExecutionType type);

	public List<IRhenaExecution> getDependencies();

	public String getAsClasspath(EExecutionType type);

	public List<URL> getAsURLs(EExecutionType type);

	public List<URL> getAsURLs();

}
