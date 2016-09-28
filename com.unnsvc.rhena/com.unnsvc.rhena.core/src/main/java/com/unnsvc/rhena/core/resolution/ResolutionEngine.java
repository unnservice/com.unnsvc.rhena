package com.unnsvc.rhena.core.resolution;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.ProjectDependencyEdge;
import com.unnsvc.rhena.core.model.RhenaNodeEdge;
import com.unnsvc.rhena.core.model.RhenaProject;

public class ResolutionEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RepositoryManager manager;
	private Stack<RhenaNodeEdge> nodeEdges = new Stack<RhenaNodeEdge>();

	public ResolutionEngine(RepositoryManager manager) {

		this.manager = manager;
	}

	public void addResolutionRequest(RhenaNodeEdge nodeEdge) {

		this.nodeEdges.add(nodeEdge);
	}

	public RhenaProject resolveModel(String componentName, String projectName, String version) throws RhenaException {

		try {
			
			ResolutionResult result = manager.resolveModel(this, new ProjectDependencyEdge(componentName, Constants.SCOPE_ITEST, projectName, version));
			
			while(!nodeEdges.isEmpty()) {
				
				manager.resolveModel(this, nodeEdges.pop());
			}
			
			return ((ProjectResolutionResult) result).getProject();
		} catch (ResolverException re) {
			
			log.error(re.getMessage(), re);
			throw new RhenaException(re);
		}
	}

}
