
package com.unnsvc.rhena.common;

import java.util.List;
import java.util.Set;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.listener.IContextEvent;
import com.unnsvc.rhena.common.listener.IContextListener;
import com.unnsvc.rhena.common.logging.IRhenaLogger;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;

/**
 * @TODO Resolver shouldn't be resolution context, which it is now with
 *       RhenaResolutionContext, resolution context needs to be separate?
 * 
 * @author noname
 *
 */
public interface IRhenaContext {

	public IRhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException;

	public IRhenaExecution materialiseExecution(IRhenaModule model, EExecutionType type) throws RhenaException;

	public List<IRepository> getRepositories();

	public Set<IRhenaEdge> getEdges();

	public void addListener(IContextListener listener);

	public IRhenaLogger getLogger(Class<?> type);

	public void fireEvent(IContextEvent event) throws RhenaException;

} 
