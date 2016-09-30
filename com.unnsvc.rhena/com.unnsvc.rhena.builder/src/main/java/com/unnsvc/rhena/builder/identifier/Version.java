
package com.unnsvc.rhena.builder.identifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unnsvc.rhena.builder.exceptions.RhenaException;

public class Version {

	public static final String VERSION_PATTERN = "^((?<major>\\d+)(\\.(?<minor>\\d+)(\\.(?<micro>\\d+))?)?)$";
	public static final Pattern VERSION = Pattern.compile(VERSION_PATTERN);

	private int major;
	private int minor;
	private int micro;

	private Version(int major, int minor, int micro) {

		this.major = major;
		this.minor = minor;
		this.micro = micro;
	}

	public static Version valueOf(String versionString) throws RhenaException {

		Matcher matcher = VERSION.matcher(versionString);
		if (matcher.matches()) {

			int major = vOrNull(matcher.group("major"));
			int minor = vOrNull(matcher.group("minor"));
			int micro = vOrNull(matcher.group("micro"));

			return new Version(major, minor, micro);
		}

		throw new RhenaException("Invalid version: " + versionString);
	}

	private static int vOrNull(String number) {

		if (number == null) {
			return -1;
		}

		return Integer.valueOf(number);
	}
	
	@Override
	public String toString() {

		return major + (minor == -1 ? "" : "." + minor + (micro == -1 ? "" : "." + micro));
	}

}
