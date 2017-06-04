
package com.unnsvc.rhena.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.config.settings.ConfigParser;
import com.unnsvc.rhena.config.settings.ConfigSerialiser;

public class TestSettingsSerialisation {

	@Test
	public void testParser() throws Exception {

		URL location = TestSettingsSerialisation.class.getResource("/TEST-INF/settings.xml");

		IRhenaConfiguration config = ConfigParser.parseConfig(location);
		Assert.assertFalse(config.getRepositoryConfiguration().getCacheRepositories().isEmpty());
		Assert.assertFalse(config.getRepositoryConfiguration().getRemoteRepositories().isEmpty());
		Assert.assertFalse(config.getRepositoryConfiguration().getWorkspaceRepositories().isEmpty());
	}

	@Test
	public void testSerialisation() throws Exception {

		URL location = TestSettingsSerialisation.class.getResource("/TEST-INF/settings.xml");
		IRhenaConfiguration config = ConfigParser.parseConfig(location);

		StringBuilder serialised = new StringBuilder();

		ConfigSerialiser.serialiseConfig(config, (indent, line) -> {

			serialised.append(ConfigSerialiser.indents(indent) + line + "\n");
		});

		StringBuilder original = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(location.openStream()))) {

			String buff = null;
			while ((buff = reader.readLine()) != null) {
				original.append(buff).append("\n");
			}
		}

		Assert.assertEquals(original.toString(), serialised.toString());
	}
}
