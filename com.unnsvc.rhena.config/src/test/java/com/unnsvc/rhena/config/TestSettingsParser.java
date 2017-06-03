package com.unnsvc.rhena.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.config.IRhenaConfiguration;
import com.unnsvc.rhena.config.settings.RhenaSettingsParser;
import com.unnsvc.rhena.config.settings.RhenaSettingsSerialiser;

public class TestSettingsParser {

	@Test
	public void testSettingsParser() throws Exception {
		
		RhenaRepositoryConfiguration repoConfig = new RhenaRepositoryConfiguration();
		RhenaSettingsParser parser = new RhenaSettingsParser(repoConfig);
		parser.parseSettings(new File("src/test/resources/settings.xml"));
		
		Assert.assertNotNull(repoConfig.getCacheRepositories().isEmpty());
		Assert.assertEquals(1, repoConfig.getRemoteRepositories().size());
		Assert.assertEquals(1, repoConfig.getWorkspaceRepositories().size());
	}
	
	
	@Test
	public void testSettingsSerialiser() throws Exception {
		
		IRhenaConfiguration config = new RhenaConfiguration();
		RhenaRepositoryConfiguration repoConfig = new RhenaRepositoryConfiguration();
		config.setRepositoryConfiguration(repoConfig);
		
		RhenaSettingsParser parser = new RhenaSettingsParser(repoConfig);
		parser.parseSettings(new File("src/test/resources/settings.xml"));
		
		
		StringBuilder sb = new StringBuilder();
		try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("src/test/resources/settings.xml")))) {
			
			int buff = -1;
			while((buff = bis.read()) != -1) {
				sb.append((char) buff);
			}
		}
		
		RhenaSettingsSerialiser serialiser = new RhenaSettingsSerialiser(config);
		System.err.println("Serialising to: ");
		System.err.print(serialiser.serialise());
		Assert.assertEquals(sb.toString(), serialiser.serialise());
	}
}
