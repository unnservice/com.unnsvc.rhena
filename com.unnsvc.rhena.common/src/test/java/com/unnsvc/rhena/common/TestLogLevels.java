
package com.unnsvc.rhena.common;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.logging.ELogLevel;

public class TestLogLevels {

	@Test
	public void testLogLevel() throws Exception {

		Assert.assertTrue(ELogLevel.INFO.isGreaterOrEquals(ELogLevel.DEBUG));

		Assert.assertTrue(ELogLevel.ERROR.isGreaterOrEquals(ELogLevel.INFO));
		
	}
}
