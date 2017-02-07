
package com.unnsvc.rhena.cmdline.flags;

public class ArgumentFlag {

	private String flag;
	private String argument;

	public ArgumentFlag(String flag, String argument) {

		this.flag = flag;
		this.argument = argument;
	}

	public String getFlag() {

		return flag;
	}

	public String getArgument() {

		return argument;
	}
}
