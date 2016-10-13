
package com.unnsvc.rhena.lifecycle.misc;

import java.io.IOException;
import java.io.Writer;

import org.slf4j.Logger;

public class LoggingPrintWriter extends Writer {

	private Logger log;
	private FileDescriptor fd;

	public LoggingPrintWriter(Logger logger, FileDescriptor fd) {

		this.log = logger;
		this.fd = fd;
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {

		if (fd == FileDescriptor.OUT) {
			log.info(new String(cbuf, off, len));
		} else if (fd == FileDescriptor.ERR) {
			log.error(new String(cbuf, off, len));
		}
	}

	@Override
	public void flush() throws IOException {

	}

	@Override
	public void close() throws IOException {

	}

	public enum FileDescriptor {

		OUT, ERR
	}
}
