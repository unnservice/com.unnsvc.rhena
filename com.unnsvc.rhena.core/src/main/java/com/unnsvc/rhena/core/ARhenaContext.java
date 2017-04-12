
package com.unnsvc.rhena.core;

import java.net.InetSocketAddress;

import com.unnsvc.rhena.agent.AgentClient;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.agent.IAgentClient;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.ObjectServerException;

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

				try {
					// needs to start the server too eh?
					AgentClient agent = new AgentClient(new InetSocketAddress(config.getAgentConfiguration().getAgentPort()));
					setAgent(agent);
				} catch (ObjectServerException ose) {
					throw new RhenaException(ose);
				}
			} else {
				System.err.println(getClass().getName() + " Not starting any agent");
			}
		}
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
