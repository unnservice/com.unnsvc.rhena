package com.unnsvc.rhena.core.identifier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.RhenaParserException;

public class Version {

	private static Logger log = LoggerFactory.getLogger(Version.class);

	private int major;
	private int minor;
	private int micro;
	private boolean snapshot;

	public static final String VERSION = "^(?<major>\\d+)(\\.(?<minor>\\d+)(\\.(?<micro>\\d+)(-(?<snapshot>SNAPSHOT))?)?)?$";
	public static final Pattern VERSION_PATTERN = Pattern.compile(VERSION);

	private Version(int major, int minor, int micro, boolean snapshot) {

		this.major = major;
		this.minor = minor;
		this.micro = micro;
		this.snapshot = snapshot;
	}

	@Override
	public String toString() {

		return major + "." + minor + "." + micro + (snapshot == true ? "-SNAPSHOT" : "");
	}

	public static Version valueOf(String versionString) throws RhenaParserException {

		Matcher m = VERSION_PATTERN.matcher(versionString);

		if (m.find()) {

			try {

				int major = Integer.valueOf(m.group("major"));
				int minor = Integer.valueOf(m.group("minor"));
				int micro = Integer.valueOf(m.group("micro"));
				boolean snapshot = m.group("snapshot") == null ? false : true;

				return new Version(major, minor, micro, snapshot);

			} catch (IllegalArgumentException iae) {
				throw new RhenaParserException(iae);
			}
		} else {

			throw new RhenaParserException("Invalid version pattern: " + versionString);
		}
	}
}
