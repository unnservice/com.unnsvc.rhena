
package com.unnsvc.rhena.common.model;

public enum RhenaExecutionType {

	COMPILE, 
	TEST(COMPILE, new RhenaExecutionType[] {COMPILE}), 
	PROTOTYPE(TEST, new RhenaExecutionType[] {COMPILE, TEST}), 
	ITEST(TEST, new RhenaExecutionType[] {COMPILE, TEST});

	private RhenaExecutionType dependency;
	private RhenaExecutionType[] transitiveOver;

	RhenaExecutionType(RhenaExecutionType dependency, RhenaExecutionType... transitiveOver) {

		this.dependency = dependency;
		this.transitiveOver = transitiveOver;
	}

	RhenaExecutionType() {

	}

	public boolean isTransitiveOver(RhenaExecutionType other) {

		if(this == other) {
			return true;
		}
		
		for(RhenaExecutionType canTraverse : transitiveOver) {
			
			if(canTraverse == other) {
				return true;
			}
		}
		return false;
	}

	public RhenaExecutionType getDependency() {

		return dependency;
	}

	public String toLabel() {

		return toString().toLowerCase();
	}
}
