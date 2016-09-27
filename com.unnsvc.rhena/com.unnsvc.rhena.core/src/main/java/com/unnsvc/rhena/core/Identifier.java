package com.unnsvc.rhena.core;

import java.util.regex.Pattern;

public class Identifier {

	public static final String IDENTIFIER = "[a-Z0-9](\\.[a-Z0-9])*";
	public static final Pattern IDENTIFIER_PATTERN = Pattern.compile(IDENTIFIER);
	
	public Identifier(String identifier) {
		
	}
}
