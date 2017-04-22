
package com.unnsvc.rhena.common;

import com.unnsvc.rhena.common.exceptions.RhenaException;

public interface IRhenaAgentClientFactory {

	public IRhenaAgentClient newClient(IRhenaContext context) throws RhenaException;

}
