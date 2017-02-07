
package com.unnsvc.rhena.cmdline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.cmdline.flags.ArgumentException;
import com.unnsvc.rhena.cmdline.flags.ArgumentParser;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public abstract class AbstractMain {

	private Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) throws RhenaException {

		Main m = new Main();
		m.runWithArgs(args);
	}

	public void runWithArgs(String... args) throws RhenaException {

		ArgumentParser parser = null;
		;
		try {
			parser = new ArgumentParser(args);
		} catch (ArgumentException ae) {
			log.error(ae.getMessage(), ae);
			printHelp();
			return;
		}

		runWithArgs(parser);
	}

	protected abstract void runWithArgs(ArgumentParser parser) throws RhenaException;

	private void printHelp() {

		log.error("rhenaCommand <flags> <moduleIdentifier>:<executionType> <command>");
		log.error("--workspace  -w <path>		-	One or more workspace filesystem locations");
		log.error("--repository -r <uri>		-	One or more http:// location");
	}
}
