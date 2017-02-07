
package com.unnsvc.rhena.cmdline.flags;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {

	private List<ArgumentFlag> flagArgs;
	private List<String> commands;

	public ArgumentParser(String[] args) throws ArgumentException {

		if (args.length < 1) {
			throw new ArgumentException("Command requires at least one argument.");
		}
		this.flagArgs = new ArrayList<ArgumentFlag>();
		this.commands = new ArrayList<String>();
		parse(args);
	}

	private void parse(String[] args) throws ArgumentException {

		for (int i = 0; i < args.length; i++) {

			String curr = args[i];
			if (curr.matches("^((--[^-]{2,})|(-[^-]))$")) {

				// check so there's a flag argument
				if (args.length - 1 < i + 1) {
					throw new ArgumentException("Empty argument for: " + curr);
				}

				String flagArg = args[i + 1];
				i++;

				if (flagArg.startsWith("-")) {
					throw new ArgumentException("Expected argument for: " + curr + " but got " + flagArg);
				}

				if (curr.startsWith("--")) {

					curr = curr.substring(2);
				} else {

					curr = curr.substring(1);
				}

				flagArgs.add(new ArgumentFlag(curr, flagArg));
			} else {

				commands.add(curr);
			}
		}
	}

	public List<ArgumentFlag> getFlag(String flagName, String shorthand) {

		List<ArgumentFlag> found = new ArrayList<ArgumentFlag>();
		for (ArgumentFlag flag : flagArgs) {
			if (flag.getFlag().equals(flagName) || flag.getFlag().equals(shorthand)) {
				found.add(flag);
			}
		}
		return found;
	}
}
