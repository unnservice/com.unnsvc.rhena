
package com.unnsvc.rhena.common.model;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public class ModuleIdentifier {

	private Identifier componentName;
	private Identifier moduleName;
	private Version version;

	public ModuleIdentifier(String[] moduleIdentifier) throws RhenaException {

		this(moduleIdentifier[0], moduleIdentifier[1], moduleIdentifier[2]);
	}

	public ModuleIdentifier(Identifier componentName, Identifier moduleName, Version version) {

		this.componentName = componentName;
		this.moduleName = moduleName;
		this.version = version;
	}

	public ModuleIdentifier(String componentIdentifierStr, String moduleIdentifierStr, String versionStr) throws RhenaException {

		this(Identifier.valueOf(componentIdentifierStr), Identifier.valueOf(moduleIdentifierStr), Version.valueOf(versionStr));
	}

	public Identifier getComponentName() {

		return componentName;
	}

	public Identifier getModuleName() {

		return moduleName;
	}

	public Version getVersion() {

		return version;
	}

	@Override
	public String toString() {

		return componentName.toString() + ":" + moduleName.toString() + ":" + version.toString();
	}

	public String toTag() {

		return "[" + toString() + "]";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
		result = prime * result + ((moduleName == null) ? 0 : moduleName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ModuleIdentifier other = (ModuleIdentifier) obj;
		if (componentName == null) {
			if (other.componentName != null)
				return false;
		} else if (!componentName.equals(other.componentName))
			return false;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public static ModuleIdentifier valueOf(String moduleIdentifierStr) throws RhenaException {

		String[] parts = moduleIdentifierStr.split(":");
		return new ModuleIdentifier(parts);
	}

}
