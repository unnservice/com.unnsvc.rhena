
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaAgentServer {

	public void startupAgent() throws RhenaException;

	public void shutdownAgent() throws RhenaException;

}
