
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

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleDeclaration;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.common.visitors.IModelVisitor;

/**
 * @TODO bug, merge parent only once, otherwise it will have undetermined effect
 * @author noname
 *
 */
public class ModelMergeVisitor implements IModelVisitor {

	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolutionContext context;
	private Set<ModuleIdentifier> entered;

	public ModelMergeVisitor(IResolutionContext context) {

		this.context = context;
		this.entered = new HashSet<ModuleIdentifier>();
	}

	@Override
	public void startModule(IRhenaModule module) throws RhenaException {

		if (entered.contains(module.getModuleIdentifier())) {
			return;
		}

		if (module.getParentModule() != null) {

			module.getParentModule().getTarget().visit(this); 
			
			// merge parent into child
			if (module.getParentModule() != null) {

				IRhenaModule parent = module.getParentModule().getTarget();
				mergeParent(parent, module);
				log.debug("[" + module.getModuleIdentifier() + "]:model merged parent " + parent.getModuleIdentifier());
			}
		}

		for (ILifecycleDeclaration ld : module.getLifecycleDeclarations().values()) {

			ld.getConfigurator().getTarget().visit(this);

			for (IProcessorReference pr : ld.getProcessors()) {

				pr.getTarget().visit(this);
			}

			ld.getGenerator().getTarget().visit(this);
		}
		
		if (module.getLifecycleName() != null) {

			// the model is merged so we can just query this module
			if (!module.getLifecycleDeclarations().containsKey(module.getLifecycleName())) {
				throw new RhenaException(module.getModuleIdentifier().toTag() + ": Lifecycle name " + module.getLifecycleName() + " is not found");
			}
		}

		for (IRhenaEdge edge : module.getDependencyEdges()) {

			edge.getTarget().visit(this);
		}

		this.entered.add(module.getModuleIdentifier());
	}

	@Override
	public void endModule(IRhenaModule model) throws RhenaException {

	}

	/**
	 * @TODO Assert dependency precedence andkey
	 * @param parent
	 * @param model
	 */
	private void mergeParent(IRhenaModule parent, IRhenaModule model) {

		// Merge properties
		Properties mergedProperties = new Properties();
		mergedProperties.putAll(parent.getProperties());
		mergedProperties.putAll(model.getProperties());
		model.setProperties(mergedProperties);

		// Merge dependencies
		List<IRhenaEdge> dependencyEdges = new ArrayList<IRhenaEdge>();
		dependencyEdges.addAll(parent.getDependencyEdges());
		for (IRhenaEdge edge : model.getDependencyEdges()) {
			if (!dependencyEdges.contains(edge)) {
				dependencyEdges.addAll(model.getDependencyEdges());
			}
		}
		model.setDependencyEdges(dependencyEdges);

		// Merge lifecycles
		Map<String, ILifecycleDeclaration> lifecycles = new HashMap<String, ILifecycleDeclaration>();
		lifecycles.putAll(parent.getLifecycleDeclarations());
		lifecycles.putAll(model.getLifecycleDeclarations());
		model.setLifecycleDeclarations(lifecycles);
	}

}
