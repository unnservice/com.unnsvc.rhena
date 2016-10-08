
package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.CompositeScope;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.Utils;

public class RhenaModelMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaContext context;
	private Map<ModuleIdentifier, RhenaModule> moduleModels;
	private Map<ModuleIdentifier, Map<CompositeScope, RhenaLifecycleExecution>> moduleExecutions;

	public RhenaModelMaterialiser(RhenaContext context) {

		this.context = context;
		this.moduleModels = new HashMap<ModuleIdentifier, RhenaModule>();
		this.moduleExecutions = new HashMap<ModuleIdentifier, Map<CompositeScope, RhenaLifecycleExecution>>();
	}

	public RhenaModule materialiseModel(ModuleIdentifier moduleIdentifier) throws RhenaException {

		if (moduleModels.containsKey(moduleIdentifier)) {
			return moduleModels.get(moduleIdentifier);
		}

		RhenaModule module = context.getResolutionManager().materialiseModel(moduleIdentifier);
		moduleModels.put(moduleIdentifier, module);

		log.debug("Materialised model of: " + moduleIdentifier + (log.isDebugEnabled() ? " [stack trace count: " + Utils.stackTraceCount() + "]" : ""));

		return module;
	}

	public RhenaLifecycleExecution materialiseScope(ModuleIdentifier moduleIdentifier, CompositeScope scope) throws RhenaException {

		if (moduleExecutions.containsKey(moduleIdentifier) && moduleExecutions.get(moduleIdentifier).containsKey(scope)) {
			
			return moduleExecutions.get(moduleIdentifier).get(scope);
		}

		if(scope.getDependency() != null) {
			
			materialiseScope(moduleIdentifier, scope.getDependency());
		}
		
		RhenaModule model = materialiseModel(moduleIdentifier);
		log.info("Executing: " + moduleIdentifier + ":" + scope.toString().toLowerCase());
		RhenaLifecycleExecution executedModule = model.getRepository().materialiseScope(model, scope);
		if(moduleExecutions.get(moduleIdentifier) == null) {
			moduleExecutions.put(moduleIdentifier, new HashMap<CompositeScope, RhenaLifecycleExecution>());
		}
		moduleExecutions.get(moduleIdentifier).put(scope,  executedModule);
		return executedModule;
	}

}
