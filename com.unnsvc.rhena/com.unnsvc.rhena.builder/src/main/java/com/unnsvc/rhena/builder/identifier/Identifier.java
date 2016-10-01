
package com.unnsvc.rhena.builder.identifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unnsvc.rhena.builder.exceptions.RhenaException;

public class Identifier {

	public static final String IDENTIFIER_PATTERN = "^[a-zA-Z0-9]+(\\.a-zA-Z0-9)?$";
	public static final Pattern IDENTIFIER = Pattern.compile(IDENTIFIER_PATTERN);
	private String identifier;

	private Identifier(String identifier) {

		this.identifier = identifier;
	}

	public static Identifier valueOf(String identifier) throws RhenaException {

		Matcher matcher = IDENTIFIER.matcher(identifier);
		if (matcher.matches()) {
			return new Identifier(identifier);
		}
		throw new RhenaException("Invalid identifier: " + identifier);
	}

	@Override
	public String toString() {

		return identifier;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
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
		Identifier other = (Identifier) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}
}
