
package com.unnsvc.rhena.common;

import org.junit.Before;
import com.unnsvc.rhena.common.ng.IRhenaCache;

public abstract class AbstractRhenaTest {

	private IRhenaCache mockCache;

	@Before
	public void before() {

		this.mockCache = new MockCache();
	}

	public IRhenaCache getMockCache() {

		return mockCache;
	}
}
