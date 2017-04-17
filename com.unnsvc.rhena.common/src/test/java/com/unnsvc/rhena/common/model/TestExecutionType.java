
package com.unnsvc.rhena.common.model;

import org.junit.Assert;
import org.junit.Test;

public class TestExecutionType {

	@Test
	public void testExecutionType() {

		Assert.assertTrue(EExecutionType.MAIN.lessOrEqualTo(EExecutionType.TEST));
		Assert.assertTrue(EExecutionType.TEST.greaterOrEqualTo(EExecutionType.MAIN));
		Assert.assertTrue(EExecutionType.MAIN.greaterOrEqualTo(EExecutionType.MAIN));
	}
}
