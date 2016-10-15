
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.execution.EExecutionType;

public class TestExecutionType {

	@Test
	public void testExecutionTypes() {

		// Don't think it will be necessary to test all combination
		Assert.assertTrue(EExecutionType.PROTOTYPE.canTraverse(EExecutionType.MODEL));
		Assert.assertFalse(EExecutionType.DELIVERABLE.canTraverse(EExecutionType.PROTOTYPE));
		Assert.assertTrue(EExecutionType.INTEGRATION.canTraverse(EExecutionType.INTEGRATION));
		Assert.assertTrue(EExecutionType.PROTOTYPE.canTraverse(EExecutionType.TEST));
		Assert.assertFalse(EExecutionType.TEST.canTraverse(EExecutionType.PROTOTYPE));
	}
}
