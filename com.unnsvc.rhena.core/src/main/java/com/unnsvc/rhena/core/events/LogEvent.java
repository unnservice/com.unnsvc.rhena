
package com.unnsvc.rhena.core.events;

import java.util.Date;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.logging.ELogLevel;

public class LogEvent implements IContextEvent {

	private Date timestamp;
	private ELogLevel level;
	private String loggerName;
	private ModuleIdentifier identifier;
	private String message;
	private Throwable throwable;

	public LogEvent(ELogLevel level, String loggerName, ModuleIdentifier identifier, String message, Throwable throwable) {

		this.timestamp = new Date();
		this.level = level;
		this.loggerName = loggerName;
		this.identifier = identifier;
		this.message = message;
		this.throwable = throwable;
	}

	public LogEvent(ELogLevel level, Class<?> loggerName, ModuleIdentifier identifier, String message) {

		this(level, loggerName.getName(), identifier, message, null);
	}

	public Date getTimestamp() {

		return timestamp;
	}

	public ELogLevel getLevel() {

		return level;
	}

	public String getClazz() {

		return loggerName;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public String getMessage() {

		return message;
	}

	public Throwable getThrowable() {

		return throwable;
	}

	@Override
	public String toString() {

		return "LogEvent [" + getLevel().toString() + " [" + shorten(getClazz()) + "] " + message;
	}

	private String shorten(String name) {

		int width = 30;

		if (name.length() > 30) {

			return new String(name.getBytes(), name.length() - width, width);
		}

		return name;
	}
}
