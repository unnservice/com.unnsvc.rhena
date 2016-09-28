package com.unnsvc.rhena.core.identifier;

import java.util.regex.Pattern;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class Identifier {

	public static final String IDENTIFIER_STR = "[a-zA-Z0-9-_]+";
	public static final String IDENTIFIER = "^" + IDENTIFIER_STR + "(\\." + IDENTIFIER_STR + ")*$";
	public static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER);

	private String identifier;

	public static boolean matches(String str) {
		
		return IDENTIFIER_PATTERN.matcher(str).matches();
	}
	
	private Identifier(String identifier) {

		this.identifier = identifier;
	}

	@Override
	public String toString() {

		return identifier;
	}

	public static Identifier valueOf(String identifierString) throws RhenaParserException {

		if (IDENTIFIER_PATTERN.matcher(identifierString).find()) {
			return new Identifier(identifierString);
		} else {
			throw new RhenaParserException("Invalid identifier string: " + identifierString);
		}
	}
}
