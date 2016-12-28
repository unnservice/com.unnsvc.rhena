
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.listener.IContextListener;

public interface IListenerConfiguration {

	public void addListener(IContextListener<? extends IContextEvent> listener);

	public <T extends IContextEvent> void fireListener(T event) throws RhenaException;
}
