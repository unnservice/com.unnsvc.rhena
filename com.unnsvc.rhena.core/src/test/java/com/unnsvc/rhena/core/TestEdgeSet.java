
package com.unnsvc.rhena.core;

import org.junit.Assert;
import org.junit.Test;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.TraverseType;

public class TestEdgeSet {

	@Test
	public void testUnique() throws Exception {

		EdgeSet set = new EdgeSet();
		set.addEdge(TestUtil.createEdge(EExecutionType.DELIVERABLE));
		set.addEdge(TestUtil.createEdge(EExecutionType.INTEGRATION));

		Assert.assertFalse(set.isEmpty());
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.contains(TestUtil.createEdge(EExecutionType.INTEGRATION)));
	}

	@Test
	public void testUniqueReverse() throws Exception {

		EdgeSet set = new EdgeSet();
		Assert.assertTrue(set.addEdge(TestUtil.createEdge(EExecutionType.INTEGRATION)));
		Assert.assertFalse(set.addEdge(TestUtil.createEdge(EExecutionType.DELIVERABLE)));

		Assert.assertFalse(set.isEmpty());
		Assert.assertEquals(1, set.size());
		Assert.assertTrue(set.contains(TestUtil.createEdge(EExecutionType.INTEGRATION)));
	}
	
	@Test
	public void testTwo() throws Exception {
		
		EdgeSet set = new EdgeSet();
		set.addEdge(TestUtil.createEdge(EExecutionType.FRAMEWORK));
		set.addEdge(TestUtil.createEdge(EExecutionType.DELIVERABLE));
		
		Assert.assertFalse(set.isEmpty());
		Assert.assertEquals(2,  set.size());
		Assert.assertTrue(set.contains(TestUtil.createEdge(EExecutionType.FRAMEWORK)));
		Assert.assertTrue(set.contains(TestUtil.createEdge(EExecutionType.DELIVERABLE)));
	}

	@Test
	public void testDifferentTraverse() throws Exception {
		
		EdgeSet set = new EdgeSet();
		set.addEdge(TestUtil.createEdge(EExecutionType.DELIVERABLE, TraverseType.COMPONENT));
		set.addEdge(TestUtil.createEdge(EExecutionType.DELIVERABLE, TraverseType.DIRECT));
		Assert.assertEquals(2, set.size());
	}

}
