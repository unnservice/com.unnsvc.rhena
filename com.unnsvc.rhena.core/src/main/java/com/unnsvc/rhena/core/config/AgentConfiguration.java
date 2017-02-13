
package com.unnsvc.rhena.core.config;

import com.unnsvc.rhena.common.config.IAgentConfiguration;

public class AgentConfiguration implements IAgentConfiguration {

	private String agentClasspath;
	private String profilerClasspath;

	@Override
	public void setAgentClasspath(String agentClasspath) {

		this.agentClasspath = agentClasspath;
	}

	@Override
	public String getAgentClasspath() {

		return agentClasspath;
	}

	@Override
	public void setProfilerClasspath(String profilerClasspath) {

		this.profilerClasspath = profilerClasspath;
	}

	@Override
	public String getProfilerClasspath() {

		return profilerClasspath;
	}
}
