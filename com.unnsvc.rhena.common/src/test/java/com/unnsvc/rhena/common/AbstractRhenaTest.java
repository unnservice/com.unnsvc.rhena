
package com.unnsvc.rhena.common;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.ng.IRhenaCache;

public abstract class AbstractRhenaTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IRhenaCache mockCache;

	@Before
	public void before() throws Exception {

		this.mockCache = new MockCache();
	}

	public IRhenaCache getMockCache() {

		return mockCache;
	}
}
