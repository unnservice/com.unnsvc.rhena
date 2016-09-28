package com.unnsvc.rhena.core.resolution;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.core.exceptions.ResolverException;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.model.RhenaProject;

public class ResolutionEngine {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RepositoryManager manager;
	private Stack<ResolutionRequest> resolutionRequests;

	public ResolutionEngine(RepositoryManager manager) {

		this.manager = manager;
		this.resolutionRequests = new Stack<ResolutionRequest>();
	}

	public void addResolutionRequest(ResolutionRequest resolutionRequest) {

		this.resolutionRequests.add(resolutionRequest);
	}

	public RhenaProject generateModel(ResolutionRequest resolutionRequest) throws RhenaException {

		try {
			
			RhenaProject project = manager.resolveProject(resolutionRequest);
			
			
			return project;
			
//			ResolutionResult result = manager.resolveModel(this, new ProjectDependencyEdge(componentName, Constants.SCOPE_DEFAULT, projectName, version));
//			
//			while(!nodeEdges.isEmpty()) {
//				
//				manager.resolveModel(this, nodeEdges.pop());
//			}
//			
//			return ((ProjectResolutionResult) result).getProject();
		} catch (ResolverException re) {
			
			log.error(re.getMessage(), re);
			throw new RhenaException(re);
		}
	}
}
