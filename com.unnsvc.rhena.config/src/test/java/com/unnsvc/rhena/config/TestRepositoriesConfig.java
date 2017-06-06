
package com.unnsvc.rhena.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.config.IRepositoryConfiguration;
import com.unnsvc.rhena.common.repository.ERepositoryType;
import com.unnsvc.rhena.common.repository.IRepositoryDefinition;
import com.unnsvc.rhena.common.repository.RepositoryIdentifier;
import com.unnsvc.rhena.config.userconf.RepositoriesUserConfigParser;
import com.unnsvc.rhena.config.userconf.UserConfigFactory;

public class TestRepositoriesConfig {

	private static final File testConfig = new File("src/test/resources/TEST-INF/repositories.xml");

	@Test
	public void testLoadRepositories() throws Exception {

		IRepositoryConfiguration repoConfig = UserConfigFactory.readRepositories(testConfig);

		IRepositoryConfiguration repoConfigOrig = new RepositoryConfiguration();
		repoConfigOrig.addCacheRepository(repoDef(ERepositoryType.CACHE, "file://test"));
		repoConfigOrig.addWorkspaceRepositories(repoDef(ERepositoryType.WORKSPACE, "file://test"));
		repoConfigOrig.addRemoteRepository(repoDef(ERepositoryType.REMOTE, "http://test"));

		Assert.assertTrue(repoConfigOrig.equals(repoConfig));
	}

	@Test
	public void testSaveRepositories() throws Exception {

		String read = readFile(testConfig);
		IRepositoryConfiguration repoConfig = UserConfigFactory.readRepositories(testConfig);
		RepositoriesUserConfigParser parser = new RepositoriesUserConfigParser();
		String content = new String(parser.serialise(repoConfig));
		Assert.assertEquals(read, content);
	}

	private String readFile(File file) throws FileNotFoundException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (FileInputStream fis = new FileInputStream(file)) {
			int buff = -1;
			while ((buff = fis.read()) != -1) {
				baos.write(buff);
			}
		}
		return new String(baos.toByteArray());
	}

	private IRepositoryDefinition repoDef(ERepositoryType type, String uri) {

		RepositoryIdentifier id = new RepositoryIdentifier(uri);
		RepositoryDefinition def = new RepositoryDefinition(type, id, URI.create(uri));
		return def;
	}
}
