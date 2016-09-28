
package com.unnsvc.rhena.core.identifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class Version implements Comparable<Version> {

	private static Logger log = LoggerFactory.getLogger(Version.class);

	private int major;
	private int minor;
	private int micro;
	private boolean snapshot = false;

	public static final String VERSION = "^(?<major>\\d+)(\\.(?<minor>\\d+)(\\.(?<micro>\\d+)(-(?<snapshot>SNAPSHOT))?)?)?$";
	public static final Pattern VERSION_PATTERN = Pattern.compile(VERSION);

	public static boolean matches(String str) {
		
		return VERSION_PATTERN.matcher(str).matches();
	}
	
	private Version(int major, int minor, int micro, boolean snapshot) {

		this.major = major;
		this.minor = minor;
		this.micro = micro;
		this.snapshot = snapshot;
		if(major == 0 && minor <= 0 && micro <= 0 && snapshot) {
			throw new IllegalArgumentException("Zero version with snapshot not allowed");
		}
	}
	
	public Version() {
		
		this.major = 0;
		this.minor = 0;
		this.micro = 0;
	}

	/**
	 * Test whether the version is 0.0.0
	 * 
	 * @return
	 */
	public boolean isEmpty() {

		return major == 0 && minor <= 0 && micro <= 0 && !snapshot;
	}

	@Override
	public String toString() {

		return major + (minor == -1 ? "" : "." + minor) + (micro == -1 ? "" : "." + micro) + (snapshot == true ? "-SNAPSHOT" : "");
	}

	public static Version valueOf(String versionString) throws RhenaParserException {

		Matcher m = VERSION_PATTERN.matcher(versionString);

		if (m.find()) {

			try {

				int major = Integer.valueOf(m.group("major"));
				int minor = m.group("minor") == null ? -1 : Integer.valueOf(m.group("minor"));
				int micro = m.group("micro") == null ? -1 : Integer.valueOf(m.group("micro"));
				boolean snapshot = m.group("snapshot") == null ? false : true;

				return new Version(major, minor, micro, snapshot);

			} catch (IllegalArgumentException iae) {
				throw new RhenaParserException(iae);
			}
		} else {

			throw new RhenaParserException("Invalid version pattern: " + versionString);
		}
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + micro;
		result = prime * result + minor;
		result = prime * result + (snapshot ? 1231 : 1237);
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
		if (snapshot != other.snapshot)
			return false;
		return true;
	}

	@Override
	public int compareTo(Version other) {

		if(this.isEmpty() || other.isEmpty()) {
			return 0;
		}
		
		if (this.major > other.major) {
			return 1;
		} else if (this.major < other.major) {
			return -1;
		} else {

			if (this.minor > other.minor) {
				return 1;
			} else if (this.minor < other.minor) {
				return -1;
			} else {

				if (this.micro > other.micro) {
					return 1;
				} else if (this.micro < other.micro) {
					return -1;
				} else {

					if (this.snapshot && !other.snapshot) {
						return 1;
					} else if (!this.snapshot && other.snapshot) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}
	}
}
