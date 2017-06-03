package com.unnsvc.rhena.common.config;

import java.net.SocketAddress;

public interface IAgentConfiguration {

	public void setAgentAddress(SocketAddress agentAddress);

	public SocketAddress getAgentAddress();

	public void setThreads(int threads);

	public int getThreads();

	public int getAgentTimeout();

	public void setAgentTimeout(int agentTimeout);

}
