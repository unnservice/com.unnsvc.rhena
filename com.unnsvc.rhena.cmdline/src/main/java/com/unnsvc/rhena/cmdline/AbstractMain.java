
package com.unnsvc.rhena.cmdline;

import java.io.File;

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

		try {
			ArgumentParser parser = new ArgumentParser(args);

			configureWithArgs(parser);

		} catch (ArgumentException ae) {
			log.error(ae.getMessage(), ae);
			printHelp();
			return;
		}

	}

	protected abstract void configureWithArgs(ArgumentParser parser) throws RhenaException, ArgumentException;

	private void printHelp() {

		log.error("rhenaCommand <flags> <moduleIdentifier>:<executionType> <command>");
		log.error("----- main flags ------------------------------------------------------");
		log.error("--workspace  		-w <path>		-	One or more workspace filesystem locations");
		log.error("--repository 		-r <uri>		-	One or more http:// location");
		log.error("--packageWorkspace	-p <boolean>	-	Package workspace when building>");
		log.error("--parallel			-t <boolean>	-	Parallel compilation");
		log.error("--instanceHome		-i <path>		-	Defaults to " + new File(System.getProperty("user.home"), ".rhena"));
		log.error("----- expert flags ----------------------------------------------------");
		log.error("--agentClasspath		-a <classPath>	-	Agent classpath");
		log.error("--profilerClasspath	-b <classPath>	-	Profiler classpath");

	}
}
