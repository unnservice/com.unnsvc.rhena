
package com.unnsvc.rhena.ng.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.builder.CompositeScope;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.ng.RhenaContext;
import com.unnsvc.rhena.ng.Utils;
import com.unnsvc.rhena.ng.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.ng.model.RhenaModule;

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

	public RhenaLifecycleExecution materialiseScope(CompositeScope scope, RhenaModule model) {

		if (moduleExecutions.get(model.getModuleIdentifier()) != null && moduleExecutions.get(model.getModuleIdentifier()).get(scope) != null) {
			return moduleExecutions.get(model.getModuleIdentifier()).get(scope);
		}

		throw new UnsupportedOperationException("Unimplemented");
	}

}
