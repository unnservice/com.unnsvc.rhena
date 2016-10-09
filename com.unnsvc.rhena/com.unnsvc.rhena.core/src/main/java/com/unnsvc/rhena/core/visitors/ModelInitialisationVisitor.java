
package com.unnsvc.rhena.core.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IResolver;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModel;

public class ModelInitialisationVisitor extends ModelResolvingVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private Set<ModuleIdentifier> merged;

	public ModelInitialisationVisitor(IResolver resolver) {

		super(resolver);
		this.merged = new HashSet<ModuleIdentifier>();
	}

	@Override
	public void endModel(RhenaModel model) throws RhenaException {

		if (model.getParentModule() != null) {

			if (!merged.contains(model.getModuleIdentifier())) {
				RhenaModel parent = getResolver().materialiseModel(model.getParentModule().getTarget());
				mergeParent(parent, model);
				merged.add(model.getModuleIdentifier());
				log.info("Merged " + parent.getModuleIdentifier() + " into " + model.getModuleIdentifier());
			}
		}
	}

	/**
	 * @TODO Assert dependency precedence and
	 * @param parent
	 * @param model
	 */
	private void mergeParent(RhenaModel parent, RhenaModel model) {

		// Merge properties
		Properties mergedProperties = new Properties();
		mergedProperties.putAll(parent.getProperties());
		mergedProperties.putAll(model.getProperties());
		model.setProperties(mergedProperties);

		// Merge dependencies
		List<RhenaEdge> dependencyEdges = new ArrayList<RhenaEdge>();
		dependencyEdges.addAll(parent.getDependencyEdges());
		for (RhenaEdge edge : model.getDependencyEdges()) {
			if (!dependencyEdges.contains(edge)) {
				dependencyEdges.addAll(model.getDependencyEdges());
			}
		}
		model.setDependencyEdges(dependencyEdges);

		// Merge...

	}

}
