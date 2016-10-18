
package com.unnsvc.rhena.common.listener;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IContextListener {
	
	
	public void onEvent(IContextEvent event) throws RhenaException;

	/**
	 * Bind the context listeners to specific types
	 * 
	 * @return
	 */
	public Class<? extends IContextEvent> getType();
}
