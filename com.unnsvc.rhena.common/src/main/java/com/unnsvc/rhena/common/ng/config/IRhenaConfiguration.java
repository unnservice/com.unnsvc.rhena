package com.unnsvc.rhena.common.ng.config;


public interface IRhenaConfiguration {

	public IRepositoryConfiguration getRepositoryConfiguration();
	
	public void setRepositoryConfiguration(IRepositoryConfiguration repositoryConfiguration);

	public void setThreads(int threads);

	public int getThreads();
}
