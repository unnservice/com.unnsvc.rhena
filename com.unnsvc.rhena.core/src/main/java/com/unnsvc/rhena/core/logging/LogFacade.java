
package com.unnsvc.rhena.core.logging;

import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.logging.ELogLevel;
import com.unnsvc.rhena.common.logging.ILogger;
import com.unnsvc.rhena.core.events.LogEvent;

/**
 * @TODO performance improvements as this log is potentially very slow
 * @author noname
 *
 */
public class LogFacade implements ILogger {

	private static final long serialVersionUID = 1L;
	private IListenerConfiguration listenerConfig;

	public LogFacade(IListenerConfiguration listenerConfig) {

		this.listenerConfig = listenerConfig;
	}

	@Override
	public void info(Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(ELogLevel.INFO, clazz, identifier, message);
	}

	@Override
	public void info(Class<?> clazz, String message) {

		fireLogEvent(ELogLevel.INFO, clazz, null, message);
	}

	@Override
	public void error(Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(ELogLevel.ERROR, clazz, identifier, message);
	}

	@Override
	public void error(Class<?> clazz, String message) {

		fireLogEvent(ELogLevel.ERROR, clazz, null, message);
	}

	@Override
	public void debug(Class<?> clazz, String message) {

		fireLogEvent(ELogLevel.DEBUG, clazz, null, message);
	}

	@Override
	public void debug(Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(ELogLevel.DEBUG, clazz, identifier, message);
	}

	@Override
	public void warn(Class<?> clazz, String message) {

		fireLogEvent(ELogLevel.WARN, clazz, null, message);
	}

	@Override
	public void warn(Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(ELogLevel.WARN, clazz, identifier, message);
	}

	@Override
	public void trace(Class<?> clazz, String message) {

		fireLogEvent(ELogLevel.TRACE, clazz, null, message);
	}

	@Override
	public void trace(Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(ELogLevel.TRACE, clazz, identifier, message);
	}

	@Override
	public void fireLogEvent(ELogLevel level, Class<?> clazz, ModuleIdentifier identifier, String message, Throwable throwable) {

		try {
			listenerConfig.fireListener(new LogEvent(level, clazz, identifier, message, throwable));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private void fireLogEvent(ELogLevel level, Class<?> clazz, ModuleIdentifier identifier, String message) {

		fireLogEvent(level, clazz, identifier, message, null);
	}
}
