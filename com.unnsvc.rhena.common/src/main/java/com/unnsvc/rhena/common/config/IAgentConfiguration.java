
package com.unnsvc.rhena.common.config;

import java.io.Serializable;

public interface IAgentConfiguration extends Serializable {

	public void setAgentClasspath(String agentClasspath);

	public String getAgentClasspath();

	public void setProfilerClasspath(String profilerClasspath);

	public String getProfilerClasspath();

}
