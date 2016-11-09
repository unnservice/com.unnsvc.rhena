
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
import com.unnsvc.rhena.common.model.ESelectionType;

public class RhenaDependencyCollectionVisitor implements IModelVisitor {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private List<IRhenaExecution> dependencies;
	private EExecutionType executionType;
	private IRhenaContext context;
	private ESelectionType traverseType;

	public RhenaDependencyCollectionVisitor(IRhenaContext context, EExecutionType executionType, ESelectionType traverseType) {

		this(context, executionType, new ArrayList<IRhenaExecution>(), traverseType);
	}

	public RhenaDependencyCollectionVisitor(IRhenaContext resolver, EExecutionType executionType, List<IRhenaExecution> dependencies,
			ESelectionType traverseType) {

		this.executionType = executionType;
		this.dependencies = dependencies;
		this.context = resolver;
		this.traverseType = traverseType;
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		for (IRhenaEdge edge : model.getDependencies()) {

			if (edge.getExecutionType().equals(executionType)) {

				addDependency(edge);

				if (traverseType.equals(ESelectionType.NONE)) {

					// no-op .... why, oh why
				} else if (traverseType.equals(ESelectionType.COMPONENT)) {

					if (model.getIdentifier().getComponentName().equals(edge.getTarget().getComponentName())) {

						context.materialiseModel(edge.getTarget()).visit(this);
					}
				} else if (traverseType.equals(ESelectionType.SCOPE)) {

					context.materialiseModel(edge.getTarget()).visit(this);
				} else if (traverseType.equals(ESelectionType.DIRECT)) {

					for (IRhenaEdge depEdge : context.materialiseModel(edge.getTarget()).getDependencies()) {

						if (depEdge.getExecutionType().equals(executionType)) {

							dependencies.add(context.materialiseExecution(context.materialiseModel(depEdge.getTarget()), depEdge.getExecutionType()));
						}
					}
				}
			}
		}
	}

	private void addDependency(IRhenaEdge edge) throws RhenaException {

		dependencies.add(context.materialiseExecution(context.materialiseModel(edge.getTarget()), edge.getExecutionType()));
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
