
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.logging.IRhenaLogger;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @TODO Should also allow for installation..
 * 
 * @author noname
 *
 */
public class LocalCacheRepository extends RemoteRepository {

	private IRhenaLogger log;

	public LocalCacheRepository(IRhenaContext context, URI location) {

		super(context, location);
	}

	/**
	 * 
	 * @param module
	 * @param execution
	 */
	public void publish(IRhenaModule module, IRhenaExecution execution) {

	}

}
