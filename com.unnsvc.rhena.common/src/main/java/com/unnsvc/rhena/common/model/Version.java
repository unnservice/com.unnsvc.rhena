
package com.unnsvc.rhena.common.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unnsvc.rhena.common.exceptions.RhenaException;

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

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + micro;
		result = prime * result + minor;
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
		Version other = (Version) obj;
		if (major != other.major)
			return false;
		if (micro != other.micro)
			return false;
		if (minor != other.minor)
			return false;
		return true;
	}

}
