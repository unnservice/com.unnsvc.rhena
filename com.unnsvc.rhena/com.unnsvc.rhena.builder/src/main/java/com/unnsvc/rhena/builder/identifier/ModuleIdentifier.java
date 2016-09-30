
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
		if(parts.length != 3) {
			throw new RhenaException("Invalid module identifier: " + moduleIdentifier);
		}
		return new ModuleIdentifier(Identifier.valueOf(parts[0]), Identifier.valueOf(parts[1]), Version.valueOf(parts[2]));
	}
	
	@Override
	public String toString() {
	
		return componentName.toString() + ":" + moduleName.toString() + ":" + version.toString();
	}

	public String toFilename() {

		return componentName.toString() + "." + moduleName.toString(); 
	}
}
