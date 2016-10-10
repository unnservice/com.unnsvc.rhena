
package com.unnsvc.rhena.core.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.IModelVisitor;
import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.common.lifecycle.ProcessorReference;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaModule;

public class ModelMergeVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private Set<ModuleIdentifier> merged;

	public ModelMergeVisitor(IResolutionContext context) {

		this.context = context;
		this.merged = new HashSet<ModuleIdentifier>();
	}

	@Override
	public void startModel(RhenaModule model) throws RhenaException {

		if(model.getParentModule() != null) {
			
			model.getParentModule().visit(this);
		}
		
		for(LifecycleDeclaration ld : model.getLifecycleDeclarations().values()) {
			
			for(ProcessorReference pr : ld.getProcessors()) {
				
				pr.getModule().visit(this);
			}
			
			ld.getGenerator().getModule().visit(this);
		}
		
		for(RhenaEdge edge : model.getDependencyEdges()) {
			
			edge.getTarget().visit(this);
		}
	}

	@Override
	public void endModel(RhenaModule model) throws RhenaException {

		// merge parent into child
		if (model.getParentModule() != null) {

			if (!merged.contains(model.getModuleIdentifier())) {
				RhenaModule parent = model.getParentModule();
				mergeParent(parent, model);
				merged.add(model.getModuleIdentifier());
				log.debug("[" + model.getModuleIdentifier() + "]:model merged parent " + parent.getModuleIdentifier());
			}
		}

		if (model.getLifecycleName() != null) {

			// the model is merged so we can just query this module
			if (!model.getLifecycleDeclarations().containsKey(model.getLifecycleName())) {
				throw new RhenaException(model.getModuleIdentifier().toTag() + ": Lifecycle name " + model.getLifecycleName() + " is not found");
			}
		}
	}

	/**
	 * @TODO Assert dependency precedence andkey
	 * @param parent
	 * @param model
	 */
	private void mergeParent(RhenaModule parent, RhenaModule model) {

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

		// Merge lifecycles
		Map<String, LifecycleDeclaration> lifecycles = new HashMap<String, LifecycleDeclaration>();
		lifecycles.putAll(parent.getLifecycleDeclarations());
		lifecycles.putAll(model.getLifecycleDeclarations());
		model.setLifecycleDeclarations(lifecycles);
	}

}
