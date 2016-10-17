
package com.unnsvc.rhena.core;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.NotExistsException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.configuration.RhenaConfiguration;
import com.unnsvc.rhena.core.resolution.CachingResolutionContext;
import com.unnsvc.rhena.core.resolution.RemoteRepository;
import com.unnsvc.rhena.core.resolution.WorkspaceRepository;

public class TestModelRepository {

	private IRhenaContext context;
	private RemoteRepository remote;

	@Before
	public void before() throws Exception {

		context = new CachingResolutionContext(new RhenaConfiguration());
		remote = new RemoteRepository(context, RhenaConstants.LOCAL_REPOSITORY_PATH.toURI());
		context.getRepositories().add(remote);
	}

	@Test
	public void testContextMaterialises() throws Exception {

		String commonModule = "com.unnsvc.rhena:common:0.0.1";
		IRhenaModule module = context.materialiseModel(ModuleIdentifier.valueOf(commonModule));
		Assert.assertNotNull(module);
		Assert.assertEquals(ModuleIdentifier.valueOf(commonModule), module.getModuleIdentifier());
	}

	@Test
	public void testRemoteRepository() throws Exception {

		IRhenaModule module = null;

		module = remote.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:common:0.0.1"));
		Assert.assertNotNull(module);
		Assert.assertEquals(ModuleIdentifier.valueOf("com.unnsvc.rhena:common:0.0.1"), module.getModuleIdentifier());

		Assert.assertNull(remote.materialiseModel(ModuleIdentifier.valueOf("nonexistent:nonexistent:0.0.1")));
	}

	@Test
	public void testWorkspaceRepository() throws Exception {

		WorkspaceRepository repo = new WorkspaceRepository(context, new File("../"));
		IRhenaModule module = repo.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:common:0.0.1"));
		Assert.assertNotNull(module);
		Assert.assertEquals(ModuleIdentifier.valueOf("com.unnsvc.rhena:common:0.0.1"), module.getModuleIdentifier());
		Assert.assertNull(repo.materialiseModel(ModuleIdentifier.valueOf("nonexistent:nonexistent:0.0.1")));
	}

	@Test(expected = NotExistsException.class)
	public void testContextNoRepository() throws Exception {

		context.getRepositories().clear();
		Assert.assertTrue(context.getRepositories().isEmpty());
		context.materialiseModel(ModuleIdentifier.valueOf("com.unnsvc.rhena:common:0.0.1"));
	}

}
