
package com.unnsvc.rhena.core.logging;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.logging.ELogLevel;

public class LogEvent implements IContextEvent {

	private long timestamp;
	private String source;
	private ELogLevel level;
	private ModuleIdentifier identifier;
	private EExecutionType type;
	private String message;

	public LogEvent(String source, ELogLevel level, ModuleIdentifier identifier, EExecutionType type, String message) {

		this.timestamp = System.currentTimeMillis();
		this.source = source;
		this.level = level;
		this.identifier = identifier;
		this.type = type;
		this.message = message;
	}

	public LogEvent(String source, ELogLevel level, String message) {

		this.source = source;
		this.level = level;
		this.message = message;
	}

	public long getTimestamp() {

		return timestamp;
	}

	public String getSource() {

		return source;
	}

	public ELogLevel getLevel() {

		return level;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public EExecutionType getType() {

		return type;
	}

	public String getMessage() {

		return message;
	}

	@Override
	public String toString() {

		return "LogEvent [source=" + source + ", level=" + level + ", identifier=" + identifier + ", type=" + type + ", message=" + message + "]";
	}

}
