
package com.unnsvc.rhena.builder.identifier;

import com.unnsvc.rhena.builder.exceptions.RhenaException;

public class ModuleIdentifier {

	private Identifier componentName;
	private Identifier moduleName;
	private Version version;

	public ModuleIdentifier(Identifier componentName, Identifier moduleName, Version version) {

		this.componentName = componentName;
		this.moduleName = moduleName;
		this.version = version;
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

	public static ModuleIdentifier valueOf(String moduleIdentifier) throws RhenaException {

		String[] parts = moduleIdentifier.split(":");
		if (parts.length != 3) {
			throw new RhenaException("Invalid module identifier: " + moduleIdentifier);
		}
		return new ModuleIdentifier(Identifier.valueOf(parts[0]), Identifier.valueOf(parts[1]), Version.valueOf(parts[2]));
	}

	@Override
	public String toString() {

		return componentName.toString() + ":" + moduleName.toString() + ":" + version.toString();
	}

	public String toModuleDirectoryName() {

		return componentName.toString() + "." + moduleName.toString();
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
}
