
package com.unnsvc.rhena.common;

import java.io.Serializable;
import java.net.URI;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @author noname
 *
 */
public interface IRepository extends Serializable {

	/**
	 * 
	 * @param moduleIdentifier
	 * @return
	 * @throws RhenaException
	 */
	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	/**
	 * 
	 * @param cache
	 *            The engine cache. Never modify the cache in the repository, it
	 *            is handled by the framework.
	 * @param entryPoint
	 * @return
	 * @throws RhenaException
	 */
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException;

	public URI getLocation();
}
