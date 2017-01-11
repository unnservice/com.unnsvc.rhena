
package com.unnsvc.rhena.common;

import java.io.Serializable;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.listener.IContextListener;

public interface IListenerConfiguration extends Serializable {

	public void addListener(IContextListener<? extends IContextEvent> listener);

	public <T extends IContextEvent> void fireListener(T event) throws RhenaException;
}
