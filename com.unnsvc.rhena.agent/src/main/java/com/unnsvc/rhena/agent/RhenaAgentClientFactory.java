
package com.unnsvc.rhena.agent;

import com.unnsvc.rhena.common.IRhenaAgentClient;
import com.unnsvc.rhena.common.IRhenaAgentClientFactory;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.objectserver.ObjectServerException;

public class RhenaAgentClientFactory implements IRhenaAgentClientFactory {

	@Override
	public IRhenaAgentClient newClient(IRhenaContext context) throws RhenaException {

		try {
			return new RhenaAgentClient(context.getConfig().getAgentAddress());
		} catch (ObjectServerException ose) {
			throw new RhenaException(ose);
		}
	}

}
