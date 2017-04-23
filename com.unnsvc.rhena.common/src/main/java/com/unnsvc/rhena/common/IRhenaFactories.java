
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.execution.IBuilderFactory;
import com.unnsvc.rhena.common.repository.IRepositoryFactory;

public interface IRhenaFactories {

	public IRhenaAgentClientFactory getAgentClientFactory();

	public IRepositoryFactory getRepositoryFactory();

	public IBuilderFactory getBuilderFactory();
}
