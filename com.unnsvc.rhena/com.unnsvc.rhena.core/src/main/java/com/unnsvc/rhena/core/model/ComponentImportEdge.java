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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComponentImportEdge other = (ComponentImportEdge) obj;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		return true;
	}

}
