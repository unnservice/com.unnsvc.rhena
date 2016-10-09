
package com.unnsvc.rhena.core.resolution;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ModuleState;
import com.unnsvc.rhena.common.model.RhenaLifecycleExecution;
import com.unnsvc.rhena.common.model.RhenaModule;
import com.unnsvc.rhena.core.RhenaContext;
import com.unnsvc.rhena.core.Utils;

public class RhenaModelMaterialiser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaContext context;
	private Map<ModuleIdentifier, RhenaModule> moduleModels;

	public RhenaModelMaterialiser(RhenaContext context) {

		this.context = context;
		this.moduleModels = new HashMap<ModuleIdentifier, RhenaModule>();
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

	public RhenaLifecycleExecution materialisePackaged(ModuleIdentifier moduleIdentifier) throws RhenaException {

		RhenaModule model = materialiseModel(moduleIdentifier);
		log.info("Materialise packaged: " + moduleIdentifier);
		RhenaLifecycleExecution executedModule = model.getRepository().materialisePackaged(model);
		return executedModule;
	}

}
