
package com.unnsvc.rhena.common.config;

import java.io.Serializable;

public interface IAgentConfiguration extends Serializable {

	public void setAgentPort(int agentPort);

	public int getAgentPort();

	public void setAgentClasspath(String agentClasspath);

	public String getAgentClasspath();

	public void setExternalAgent(boolean externalAgent);

	public boolean isExternalAgent();

}
