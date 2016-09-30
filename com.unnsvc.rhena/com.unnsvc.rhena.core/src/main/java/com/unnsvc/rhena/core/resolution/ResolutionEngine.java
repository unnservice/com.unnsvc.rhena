
package com.unnsvc.rhena.core.resolution;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.identifier.ComponentIdentifier;
import com.unnsvc.rhena.core.model.RhenaComponent;
import com.unnsvc.rhena.core.model.RhenaComponentEdge;
import com.unnsvc.rhena.core.model.RhenaDependencyEdge;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.model.Scope;

public class ResolutionEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaRepositoryManager manager;
	private Stack<RhenaComponentEdge> componentEdges;

	public ResolutionEngine(RhenaRepositoryManager manager) {

		this.manager = manager;
		this.componentEdges = new Stack<RhenaComponentEdge>();
	}

	public void addResolveComponentEdge(RhenaComponentEdge componentEdge) {

		this.componentEdges.add(componentEdge);
	}

	public RhenaProject generateModel(ComponentIdentifier componentIdentifier, String projectName, Scope scope) throws RhenaException {

		RhenaComponent component = resolveComponents(componentIdentifier);

		/**
		 * We will have a complete component view at this point so begin to resolve dependency tree
		 */
		RhenaProject project = resolveProjectTree(component.getProject(projectName));

		if (!(project instanceof RhenaProject)) {

			throw new RhenaException("Initial project is not a rhena project");
		}

		return (RhenaProject) project;
	}
	
	public RhenaProject generateModel(String componentIdentifier, String projectName, Scope scope) throws RhenaException {
		
		return generateModel(new ComponentIdentifier(componentIdentifier), projectName, scope);
	}

	private RhenaProject resolveProjectTree(RhenaProject project) {

		for(RhenaDependencyEdge dependencyEdge : project.getDependencies()) {
			
		}
		
		return project;
	}

	private RhenaComponent resolveComponents(ComponentIdentifier componentIdentifier) throws RhenaException {

		RhenaComponent component = manager.resolveComponentEdge(this, new RhenaComponentEdge(componentIdentifier)).getComponent();
		/**
		 * Resolve remaining component imports
		 * 
		 */
		while (!componentEdges.isEmpty()) {

			manager.resolveComponentEdge(this, componentEdges.pop());
		}
		
		return component;
	}
}
