
package com.unnsvc.rhena.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class CascadingModelBuilder {

	private IRhenaConfiguration config;
	private Map<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>> executions;
	private CascadingModelResolver resolver;

	public CascadingModelBuilder(IRhenaConfiguration config, CascadingModelResolver resolver) {

		this.config = config;
		this.executions = new HashMap<ModuleIdentifier, Map<EExecutionType, IRhenaExecution>>();
		this.resolver = resolver;
	}

	public IRhenaExecution buildEdge(IRhenaEdge entryPoint) throws RhenaException {

		Set<IRhenaEdge> alledges = new HashSet<IRhenaEdge>();
		alledges.add(entryPoint);
		for (IRhenaModule module : resolver.getModules().values()) {

			alledges.addAll(Utils.getAllRelationships(module));
		}

		
		Set<IRhenaEdge> resolvable = new HashSet<IRhenaEdge>();
		while (!(resolvable = selectResolved(alledges)).isEmpty()) {

			System.err.println("Selected for simultaneous execution: " + resolvable.size() + ": " + resolvable);
			
			for(IRhenaEdge edge : resolvable) {
				
				IRhenaExecution execution = materialiseExecution(edge);
				if(!executions.containsKey(edge.getTarget())) {
					executions.put(edge.getTarget(), new HashMap<EExecutionType, IRhenaExecution>());
				}
				executions.get(edge.getTarget()).put(edge.getExecutionType(), execution);
			}
		}

		return materialiseExecution(entryPoint);
	}

	private IRhenaExecution materialiseExecution(IRhenaEdge edge) throws RhenaException {

		IRhenaExecution execution = new RhenaExecution(edge.getTarget(), edge.getExecutionType(), new ArtifactDescriptor("somefile", Utils.toUrl("http://some.url"), "sha1"));
		return execution;
	}

	private Set<IRhenaEdge> selectResolved(Set<IRhenaEdge> alledges) throws RhenaException {

		Set<IRhenaEdge> selected = new HashSet<IRhenaEdge>();
		for (Iterator<IRhenaEdge> iter = alledges.iterator(); iter.hasNext();) {
			IRhenaEdge edge = iter.next();
			IRhenaModule module = resolver.materialiseModel(edge.getTarget());
			if (isBuildable(module)) {
				selected.add(edge);
				iter.remove();
			}
		}
		return selected;
	}

	private boolean isBuildable(IRhenaModule module) throws RhenaException {

		for (IRhenaEdge edge : Utils.getAllRelationships(module)) {
			boolean containsKey = executions.containsKey(edge.getTarget());
			if (!containsKey) {
				return false;
			} else if (containsKey && !executions.get(edge.getTarget()).containsKey(edge.getExecutionType())) {
				return false;
			}
		}

		return true;
	}
}
