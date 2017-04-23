
package com.unnsvc.rhena.itest;

import com.unnsvc.rhena.common.IRhenaFactories;
import com.unnsvc.rhena.core.RhenaFactories;

public abstract class AbstractIntegrationTest extends AbstractAgentTest {

	@Override
	protected IRhenaFactories createMockFactories() {

		return new RhenaFactories();
	}
}
