
package com.unnsvc.rhena.itest;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IRhenaEngine;
import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.common.execution.IExecutionResponse;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.config.RepositoryDefinition;
import com.unnsvc.rhena.core.RhenaEngine;

public class TestRhenaSelfBuild extends AbstractIntegrationTest {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void testSelfBuild() throws Exception {

		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.unnsvc.rhena:core:1.0.0");

		IRhenaEngine engine = new RhenaEngine(getConfig());

		IRhenaModule module = engine.resolveModule(identifier);
		IExecutionResponse result = engine.resolveExecution(EExecutionType.ITEST, module);
		Assert.assertNotNull(result);
		Assert.assertTrue(result instanceof IExecutionResponse);
	}
	
	@Override
	protected void configureRepositories(IRhenaConfiguration config) throws IOException {
	
		

//		File localRepo = new File("../test-repositories/localRepo").getAbsoluteFile().getCanonicalFile();
//
//		IRepositoryDefinition localRepoDef = RepositoryDefinition.newLocal(localRepo.getName(), localRepo.toURI());
//		log.debug(localRepoDef.toString());
//		config.getRepositoryConfiguration().setCacheRepository(localRepoDef);

		File workspaceRepo = new File("../../").getAbsoluteFile().getCanonicalFile();
		
		IRepositoryDefinition workspaceRepoDef = RepositoryDefinition.newWorkspace(workspaceRepo.getName(), workspaceRepo.toURI());
		log.debug(workspaceRepoDef.toString());
		config.getRepositoryConfiguration().addWorkspaceRepositories(workspaceRepoDef);
	}
}
