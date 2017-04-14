
package com.unnsvc.rhena.core;

import org.junit.Test;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.ng.IRhenaEngine;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;
import com.unnsvc.rhena.config.RhenaConfiguration;

public class TestRhenaEngine {

	@Test
	public void testEngine() throws RhenaException {
		
		IRhenaConfiguration config = new RhenaConfiguration();
		
		IRhenaEngine engine = new RhenaEngine(config);
		
	}
}
