
package com.unnsvc.rhena.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unnsvc.rhena.common.IListenerConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.listener.IContextListener;

public class ListenerConfiguration implements IListenerConfiguration {

	private static final long serialVersionUID = 1L;
	private Map<Class<? extends IContextEvent>, List<IContextListener<? extends IContextEvent>>> listeners;

	public ListenerConfiguration() {

		this.listeners = new HashMap<Class<? extends IContextEvent>, List<IContextListener<? extends IContextEvent>>>();
	}

	@Override
	public void addListener(IContextListener<? extends IContextEvent> listener) {

		List<IContextListener<? extends IContextEvent>> l = listeners.get(listener.getType());
		if (l == null) {
			l = new ArrayList<IContextListener<? extends IContextEvent>>();
			listeners.put(listener.getType(), l);
		}
		l.add(listener);
	}

	@Override
	@SuppressWarnings("all")
	public <T extends IContextEvent> void fireListener(T event) throws RhenaException {

		List l = listeners.get(event.getClass());
		if (l != null) {
			for (Object listener : l) {
				((IContextListener) listener).onEvent(event);
			}
		}
	}
}
