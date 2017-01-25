
package com.unnsvc.rhena.common;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.identity.Version;


public class TestVersion {

	@Test
	public void testVersion() throws Exception {
		
		Version v = Version.valueOf("1.0.0.TEST");
		Assert.assertEquals("1.0.0.TEST", v.toString());
		
	}
}
