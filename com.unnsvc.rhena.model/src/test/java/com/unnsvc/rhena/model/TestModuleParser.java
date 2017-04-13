package com.unnsvc.rhena.model;

import java.io.File;
import java.net.URI;

import org.junit.Test;

import com.unnsvc.rhena.common.MockCache;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.ng.model.IRhenaModule;
import com.unnsvc.rhena.common.ng.repository.RepositoryIdentifier;
import com.unnsvc.rhena.model.parser.RhenaModuleParser;

public class TestModuleParser {

	@Test
	public void testParseModule() throws Exception {
		
		File repositoryLocation = new File(new File("").getCanonicalFile(), "src/test/resources");
		File moduleLocation = new File(repositoryLocation, "com.test.module");
		URI moduleDescriptorLocation = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME).toURI();
		ModuleIdentifier identifier = ModuleIdentifier.valueOf("com.test:module:1.0.0");
		RepositoryIdentifier repositoryIdentifier = new RepositoryIdentifier(repositoryLocation.toURI());
		
		MockCache cache = new MockCache();
		RhenaModuleParser parser = new RhenaModuleParser(cache, repositoryIdentifier, identifier, moduleDescriptorLocation);
		IRhenaModule module = parser.getModule();
	}
}
