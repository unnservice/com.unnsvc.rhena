
package com.unnsvc.rhena.cmdline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.cmdline.flags.ArgumentException;
import com.unnsvc.rhena.cmdline.flags.ArgumentParser;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class Main {

	private Logger log = LoggerFactory.getLogger(getClass());

	private Main() {

	}

	public static void main(String... args) throws RhenaException {

		Main m = new Main();
		m.parse(args);
	}

	public void parse(String... args) throws RhenaException {

		ArgumentParser parser;
		try {
			parser = new ArgumentParser(args);
		} catch (ArgumentException ae) {
			printHelp();
		}
	}

	private void printHelp() {

	}
}
