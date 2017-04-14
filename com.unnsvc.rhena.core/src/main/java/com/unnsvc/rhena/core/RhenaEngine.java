
package com.unnsvc.rhena.core;

import com.unnsvc.rhena.common.ng.IRhenaEngine;
import com.unnsvc.rhena.common.ng.config.IRhenaConfiguration;

public class RhenaEngine implements IRhenaEngine {

	private IRhenaConfiguration config;

	public RhenaEngine(IRhenaConfiguration config) {

		this.config = config;
	}
}
