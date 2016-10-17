
package com.unnsvc.rhena.common.visitors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.TraverseType;

public class RhenaDependencyCollectionVisitor implements IModelVisitor {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private List<IRhenaExecution> dependencies;
	private EExecutionType executionType;
	private IRhenaContext resolver;
	private TraverseType traverseType;

	public RhenaDependencyCollectionVisitor(IRhenaContext resolver, EExecutionType executionType, TraverseType traverseType) {

		this(resolver, executionType, new ArrayList<IRhenaExecution>(), traverseType);
	}

	public RhenaDependencyCollectionVisitor(IRhenaContext resolver, EExecutionType executionType, List<IRhenaExecution> dependencies,
			TraverseType traverseType) {

		this.executionType = executionType;
		this.dependencies = dependencies;
		this.resolver = resolver;
		this.traverseType = traverseType;
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		for (IRhenaEdge edge : model.getDependencyEdges()) {

			if (edge.getExecutionType().equals(executionType)) {

				addDependency(edge);

				if (traverseType.equals(TraverseType.NONE)) {

					// no-op .... why, oh why
				} else if (traverseType.equals(TraverseType.COMPONENT)) {

					if (model.getModuleIdentifier().getComponentName().equals(edge.getTarget().getModuleIdentifier().getComponentName())) {

						edge.getTarget().visit(this);
					}
				} else if (traverseType.equals(TraverseType.SCOPE)) {

					edge.getTarget().visit(this);
				} else if (traverseType.equals(TraverseType.DIRECT)) {

					for (IRhenaEdge depEdge : edge.getTarget().getDependencyEdges()) {

						if (depEdge.getExecutionType().equals(executionType)) {

							dependencies.add(resolver.materialiseExecution(depEdge.getTarget(), depEdge.getExecutionType()));
						}
					}
				}
			}
		}
	}

	private void addDependency(IRhenaEdge edge) throws RhenaException {

		dependencies.add(resolver.materialiseExecution(edge.getTarget(), edge.getExecutionType()));
	}

	public List<IRhenaExecution> getDependencies() {

		return dependencies;
	}

	public List<URL> getDependenciesURL() throws RhenaException {

		List<URL> urls = new ArrayList<URL>();
		for (IRhenaExecution re : getDependencies()) {
			urls.add(re.getArtifact().getArtifactUrl());
		}
		return urls;
	}
}
