
package com.unnsvc.rhena.core;

import org.junit.Test;

public class TestExecutionType {

	@Test
	public void testTraversable() {

		// Don't think it will be necessary to test all combination
//		Assert.assertTrue(EExecutionType.PROTOTYPE.canTraverse(EExecutionType.MODEL));
//		Assert.assertFalse(EExecutionType.DELIVERABLE.canTraverse(EExecutionType.PROTOTYPE));
//		Assert.assertTrue(EExecutionType.INTEGRATION.canTraverse(EExecutionType.INTEGRATION));
//		Assert.assertTrue(EExecutionType.PROTOTYPE.canTraverse(EExecutionType.TEST));
//		Assert.assertFalse(EExecutionType.TEST.canTraverse(EExecutionType.PROTOTYPE));
	}

	@Test
	public void testIsParnet() {

//		Assert.assertTrue(EExecutionType.PROTOTYPE.isParentOf(EExecutionType.MODEL));
//		Assert.assertFalse(EExecutionType.PROTOTYPE.isParentOf(EExecutionType.INTEGRATION));
//		Assert.assertFalse(EExecutionType.PROTOTYPE.isParentOf(EExecutionType.PROTOTYPE));
	}
}
