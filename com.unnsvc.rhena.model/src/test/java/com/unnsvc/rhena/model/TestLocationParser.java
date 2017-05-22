package com.unnsvc.rhena.model;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.model.parser.ModelHelper;

public class TestLocationParser {

	@Test
	public void test() throws Exception {
		
		ModuleIdentifier expect = ModuleIdentifier.valueOf("com.unnsvc.rhena:model:1.0.0");
		ModuleIdentifier identifier = ModelHelper.locationToModuleIdentifier(new File("."));
		Assert.assertEquals(expect, identifier);
	}
}
