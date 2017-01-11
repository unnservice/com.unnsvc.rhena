package com.unnsvc.rhena.core.logging;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.core.events.LogEvent;

public class SystemOutLogListener implements IContextListener<LogEvent> {

	private static final long serialVersionUID = 1L;

	@Override
	public void onEvent(LogEvent event) throws RhenaException {

		System.out.println(event.toString());
	}

	@Override
	public Class<LogEvent> getType() {

		return LogEvent.class;
	}
}
