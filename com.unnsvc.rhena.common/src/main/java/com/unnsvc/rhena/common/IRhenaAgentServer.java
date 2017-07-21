
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaAgentServer {

	public void startServer() throws RhenaException;

	public void close() throws RhenaException;

}
