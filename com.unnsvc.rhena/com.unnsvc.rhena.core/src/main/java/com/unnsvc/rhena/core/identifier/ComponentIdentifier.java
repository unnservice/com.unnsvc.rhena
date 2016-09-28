package com.unnsvc.rhena.core.identifier;

import java.util.Arrays;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class ComponentIdentifier extends QualifiedIdentifier {
	
	private Identifier component;
	private Version version;

	/**
	 * This will only take full identifiers
	 * @param identifier
	 * @throws RhenaParserException
	 */
	public ComponentIdentifier(String identifier) throws RhenaParserException {
		
		String[] parts = identifier.split(":");
		if (parts.length == 2 && Identifier.matches(parts[0]) && Version.matches(parts[1])) {
			
			this.component = Identifier.valueOf(parts[0]);
			this.version = Version.valueOf(parts[1]);
		} else {
			throw new RhenaParserException("Invalid component identifier: " + Arrays.toString(parts));
		}
	}
}
