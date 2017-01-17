
package com.unnsvc.rhena.core.visitors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

public class DependencyCollectionVisitor implements IModelVisitor {

	// private Logger log = LoggerFactory.getLogger(getClass());
	private List<IRhenaExecution> dependencies;
	private EExecutionType executionType;
	private IRhenaCache cache;
	private ESelectionType traverseType;

	public DependencyCollectionVisitor(IRhenaCache cache, IRhenaEdge edge) {

		this(cache, new ArrayList<IRhenaExecution>(), edge);
	}

	public DependencyCollectionVisitor(IRhenaCache cache, List<IRhenaExecution> dependencies, IRhenaEdge edge) {

		this.cache = cache;
		this.dependencies = dependencies;
		this.executionType = edge.getEntryPoint().getExecutionType();
		this.traverseType = edge.getTraverseType();

		dependencies.add(cache.getExecution(edge.getEntryPoint().getTarget()).get(executionType));
	}

	@Override
	public void visit(IRhenaModule model) throws RhenaException {

		for (IRhenaEdge edge : model.getDependencies()) {

			if (edge.getEntryPoint().getExecutionType().equals(executionType)) {

				addDependency(edge);

				if (traverseType.equals(ESelectionType.NONE)) {

					// no-op
				} else if (traverseType.equals(ESelectionType.COMPONENT)) {

					if (model.getIdentifier().getComponentName().equals(edge.getEntryPoint().getTarget().getComponentName())) {
						// @TODO instead of "this", create a new object with the
						// new traverse type declared?
						cache.getModule(edge.getEntryPoint().getTarget()).visit(this);
						// context.materialiseModel(edge.getEntryPoint().getTarget()).visit(this);
					}
				} else if (traverseType.equals(ESelectionType.SCOPE)) {

					cache.getModule(edge.getEntryPoint().getTarget()).visit(this);
					// context.materialiseModel(edge.getEntryPoint().getTarget()).visit(this);
				} else if (traverseType.equals(ESelectionType.DIRECT)) {

					for (IRhenaEdge depEdge : cache.getModule(edge.getEntryPoint().getTarget()).getDependencies()) {

						if (depEdge.getEntryPoint().getExecutionType().equals(executionType)) {

							// dependencies.add(
							// cache.getExecution(
							// depEdge.getEntryPoint().getTarget()
							// ).get(depEdge.getEntryPoint().getExecutionType())
							// );

							addDependency(depEdge);

							// dependencies.add(
							// context.materialiseExecution(
							// context.materialiseModel(depEdge.getEntryPoint().getTarget()),
							// depEdge.getEntryPoint().getExecutionType()
							// )
							// );
						}
					}
				}
			}
		}
	}

	private void addDependency(IRhenaEdge edge) throws RhenaException {
		// System.out.println("Add dep on: " + edge);

		IRhenaExecution execution = cache.getExecution(edge.getEntryPoint().getTarget()).get(edge.getEntryPoint().getExecutionType());

		dependencies.add(execution);

		// dependencies.add(context.materialiseExecution(context.materialiseModel(edge.getEntryPoint().getTarget()),
		// edge.getEntryPoint().getExecutionType()));
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
