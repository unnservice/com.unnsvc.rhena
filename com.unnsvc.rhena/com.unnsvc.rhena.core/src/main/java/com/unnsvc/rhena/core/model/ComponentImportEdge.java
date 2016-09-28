package com.unnsvc.rhena.core.model;

public class ComponentImportEdge implements RhenaNodeEdge {

	private String componentName;
	private String resolver;

	public ComponentImportEdge(String componentName) {

		this.componentName = componentName;
		this.resolver = "default";
	}

	public String getComponentName() {

		return componentName;
	}

	@Override
	public String toString() {
		
		return "ComponentImportEdge [componentName=" + componentName + "]";
	}

	@Override
	public String getResolverName() {

		return resolver;
	}

}
