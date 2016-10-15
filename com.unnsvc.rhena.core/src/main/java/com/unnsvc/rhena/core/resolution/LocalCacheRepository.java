
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @TODO Should also allow for installation..
 * 
 * @author noname
 *
 */
public class LocalCacheRepository extends RemoteRepository {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	public LocalCacheRepository(IResolutionContext context, URI location) {

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
