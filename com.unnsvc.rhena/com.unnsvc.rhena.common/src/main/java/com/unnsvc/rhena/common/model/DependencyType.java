
package com.unnsvc.rhena.common.model;

public enum DependencyType {

	MODEL(ModuleState.MODEL), COMPILE(ModuleState.PACKAGED), TEST(ModuleState.PACKAGED), ITEST(ModuleState.PACKAGED);

	private ModuleState requiredModuleState;

	DependencyType(ModuleState requiredModuleState) {

		this.requiredModuleState = requiredModuleState;
	}

	public ModuleState getRequiredModuleState() {

		return requiredModuleState;
	}
}
