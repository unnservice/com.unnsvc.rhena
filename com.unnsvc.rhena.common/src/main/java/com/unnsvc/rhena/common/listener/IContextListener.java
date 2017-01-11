
package com.unnsvc.rhena.common.listener;

import java.io.Serializable;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IContextListener<T extends IContextEvent> extends Serializable {

	public void onEvent(T event) throws RhenaException;

	/**
	 * Bind the context listeners to specific types
	 * 
	 * @return
	 */
	public Class<T> getType();
}
