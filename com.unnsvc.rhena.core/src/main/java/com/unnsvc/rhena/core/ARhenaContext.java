
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.agent.client.AgentClient;
import com.unnsvc.rhena.agent.server.AgentServerProcess;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public abstract class ARhenaContext implements IRhenaContext {

	private IAgentClient agent;

	public ARhenaContext(IRhenaConfiguration config) throws RhenaException {

		startAgent(config);
	}

	public void startAgent(IRhenaConfiguration config) throws RhenaException {

		if (agent != null) {
			
			throw new RhenaException("We already have an agent");
		} else {

			if (!config.getAgentConfiguration().isExternalAgent()) {
				IAgentClient agent = new AgentClient(AgentServerProcess.AGENT_EXECUTION_PORT);
				agent.startup();
				setAgent(agent);
			} else {
				System.err.println(getClass().getName() + " Not starting any agent");
			}
		}
	}

	public void stopAgent() throws RhenaException {

		agent.shutdown();
	}

	@Override
	public void setAgent(IAgentClient agent) {

		this.agent = agent;
	}

	@Override
	public IAgentClient getAgent() {

		return agent;
	}
}
