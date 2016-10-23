
package com.unnsvc.rhena.common;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

/**
 * @TODO test more so we don't end up getting unexpected behavior
 * @author noname
 *
 */
public class TestReadIdentifier {

	@Test
	public void testReadIdentifier() throws Exception {

		File workspace = new File("../example-workspace");
		File project = new File(workspace, "component1.module1");
		ModuleIdentifier identifier = Utils.readModuleIdentifier(project);
		Assert.assertNotNull(identifier);
		Assert.assertEquals("component1", identifier.getComponentName().toString());
		Assert.assertEquals("module1", identifier.getModuleName().toString());
		Assert.assertEquals("0.0.1", identifier.getVersion().toString());
	}
}
