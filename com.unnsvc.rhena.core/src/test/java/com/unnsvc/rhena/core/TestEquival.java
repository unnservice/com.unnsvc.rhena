
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.model.executiontype.IDeliverableExecutionType;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;

public class TestEquival {

	@Test
	public void test() throws Exception {

		Assert.assertTrue(IExecutionType.TEST instanceof IDeliverableExecutionType);
		IDeliverableExecutionType dt = IExecutionType.TEST;
//		Assert.assertTrue(IExecutionType.DELIVERABLE.getClass().isInstance(IExecutionType.TEST));
		Assert.assertTrue(areObjectsAssignable(IExecutionType.DELIVERABLE, IExecutionType.TEST));
	}
	
	public boolean areObjectsAssignable(Object left, Object right) {
	    return left.getClass().isInstance(right);
	} 
}
