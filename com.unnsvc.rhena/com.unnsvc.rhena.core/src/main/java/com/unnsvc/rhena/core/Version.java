package com.unnsvc.rhena.core;

public class Version {

	private int major;
	private int minor;
	private int micro;
	private boolean snapshot;

	public Version(int major, int minor, int micro, boolean snapshot) {

		this.major = major;
		this.minor = minor;
		this.micro = micro;
		this.snapshot = snapshot;
	}
}
