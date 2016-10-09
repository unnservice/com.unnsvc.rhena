
package com.unnsvc.rhena.common.model;

public enum RhenaScope {

	COMPILE, TEST(COMPILE), ITEST(TEST);

	private RhenaScope dependsOn;

	RhenaScope(RhenaScope dependsOn) {

		this.dependsOn = dependsOn;
	}

	RhenaScope() {

	}

	public RhenaScope getDependsOn() {

		return dependsOn;
	}
}
