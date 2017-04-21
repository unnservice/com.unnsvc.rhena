
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaAgent {

	public void start() throws RhenaException;

	public void close() throws RhenaException;

}
