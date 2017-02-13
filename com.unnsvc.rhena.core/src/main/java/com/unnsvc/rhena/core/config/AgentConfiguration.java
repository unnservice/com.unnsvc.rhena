
package com.unnsvc.rhena.core.config;

import com.unnsvc.rhena.common.config.IAgentConfiguration;

public class AgentConfiguration implements IAgentConfiguration {

	private static final long serialVersionUID = 1L;
	private String agentClasspath;
	private String profilerClasspath;
	
	public AgentConfiguration() {
		
		this.agentClasspath = System.getProperty("java.class.path");
	}

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
