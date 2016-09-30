
package com.unnsvc.rhena.builder.identifier;

public class ComponentIdentifier {

	private Identifier componentId;

	public ComponentIdentifier(Identifier componentId) {

		this.componentId = componentId;
	}

	public Identifier getComponentId() {

		return componentId;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentId == null) ? 0 : componentId.hashCode());
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
		ComponentIdentifier other = (ComponentIdentifier) obj;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		return true;
	}
}
