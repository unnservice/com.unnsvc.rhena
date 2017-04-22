
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.agent.RhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaFactories;

public class RhenaFactories implements IRhenaFactories {

	private IRhenaAgentClientFactory agentClientFactoy;

	public RhenaFactories() {

		this.agentClientFactoy = new RhenaAgentClientFactory();
	}

	@Override
	public IRhenaAgentClientFactory getAgentClientFactory() {

		return agentClientFactoy;
	}

}
