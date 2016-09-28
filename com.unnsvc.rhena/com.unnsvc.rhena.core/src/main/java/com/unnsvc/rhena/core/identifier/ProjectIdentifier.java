package com.unnsvc.rhena.core.identifier;

import java.util.Arrays;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class ProjectIdentifier extends QualifiedIdentifier {

	private Identifier component;
	private Identifier project;
	private Version version;
	
	/**
	 * This can only take full identifiers:
	 * <pre>
	 * 	component:project:version
	 * </pre>
	 * @param parts
	 * @throws RhenaParserException
	 */
	public ProjectIdentifier(String identifier) throws RhenaParserException {

		String[] parts = identifier.split(":");
		if (parts.length == 3 && Identifier.matches(parts[0]) && Identifier.matches(parts[1]) && Version.matches(parts[2])) {
			
			this.component = Identifier.valueOf(parts[0]);
			this.project = Identifier.valueOf(parts[1]);
			this.version = Version.valueOf(parts[2]);
		} else {
			
			throw new RhenaParserException("Invalid project identifier: " + Arrays.toString(parts));
		}
	}
}
