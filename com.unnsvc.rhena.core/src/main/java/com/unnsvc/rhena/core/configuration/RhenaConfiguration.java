
package com.unnsvc.rhena.core.configuration;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;

/**
 * @TODO different locations for RHENA_HOME for windows and unix etc?
 * @author noname
 *
 */
public class RhenaConfiguration {

	private URI localCacheRepository;

	public RhenaConfiguration() {

		File userHome = new File(System.getProperty("user.home"));
		File rhenaHome = new File(userHome, ".rhena");
		localCacheRepository = new File(rhenaHome, "repository").getAbsoluteFile().toURI();
	}

	public URI getLocalCacheRepository() {

		return localCacheRepository;
	}

	public void configureLoggingAppender(Appender<ILoggingEvent> appender) {

		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.detachAndStopAllAppenders();
		rootLogger.setLevel(Level.INFO);

		ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.unnsvc.rhena");
		log.setLevel(Level.INFO);

		LoggerContext c = rootLogger.getLoggerContext();
		
		appender.setContext(c);
		appender.setName("STDOUT");

		rootLogger.addAppender(appender);

		// ConsoleAppender<ILoggingEvent> ca = new
		// ConsoleAppender<ILoggingEvent>();
		// PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		// encoder.setPattern("%d{ss.SSS} %-5level %40.40logger - %msg%n");
		// encoder.setContext(c);
		// encoder.start();
		// ca.setEncoder(encoder);
		// ca.setContext(c);
		// ca.start();

		// rootLogger.addAppender(ca);
		
//		Appender<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
//
//			@Override
//			protected void append(ILoggingEvent evt) {
//
//				LogEvent event = new LogEvent();
//				event.setLevel(evt.getLevel().toString());
//				event.setLogger(evt.getLoggerName());
//				event.setMessage(evt.getMessage());
//
//				customListener.append(event);
//			}
//		};

	}
}
