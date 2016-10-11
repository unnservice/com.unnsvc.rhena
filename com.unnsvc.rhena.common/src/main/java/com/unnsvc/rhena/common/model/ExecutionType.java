
package com.unnsvc.rhena.common.model;

public enum ExecutionType {

	COMPILE, 
	TEST(COMPILE, new ExecutionType[] {COMPILE}), 
	PROTOTYPE(TEST, new ExecutionType[] {COMPILE, TEST}), 
	ITEST(TEST, new ExecutionType[] {COMPILE, TEST});

	private ExecutionType dependency;
	private ExecutionType[] transitiveOver;

	ExecutionType(ExecutionType dependency, ExecutionType... transitiveOver) {

		this.dependency = dependency;
		this.transitiveOver = transitiveOver;
	}

	ExecutionType() {

	}

	public boolean isTransitiveOver(ExecutionType other) {

		if(this == other) {
			return true;
		}
		
		for(ExecutionType canTraverse : transitiveOver) {
			
			if(canTraverse == other) {
				return true;
			}
		}
		return false;
	}

	public ExecutionType getDependency() {

		return dependency;
	}

	public String toLabel() {

		return toString().toLowerCase();
	}
}
