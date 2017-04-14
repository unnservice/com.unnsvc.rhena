
package com.unnsvc.rhena.core.resolution;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.model.InMemoryModule;

public class InMemoryRepository implements IRepository {

	private IRhenaContext context;
	private Map<ModuleIdentifier, IRhenaModule> modules;

	public InMemoryRepository(IRhenaContext context) {

		this.context = context;
		modules = new HashMap<ModuleIdentifier, IRhenaModule>();
	}

	@Override
	public IRhenaModule materialiseModel(ModuleIdentifier identifier) throws RhenaException {

		return modules.get(identifier);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, ICaller caller) throws RhenaException {

		throw new UnsupportedOperationException("Not implemented for in-memory repository");
	}

	@Override
	public URI getLocation() {

		throw new UnsupportedOperationException("Not implemented for in-memory repository");
	}

	/**
	 * Take in-memory modules because these don't resolve beyond the model
	 * 
	 * @param module
	 */
	public void addModule(InMemoryModule module) {

		// all of its edges need to be added into the cache

		if (module.getParent() != null) {
			this.context.getCache().addEdge(module.getParent());
		}
		if (!module.getLifecycleName().equals(RhenaConstants.DEFAULT_LIFECYCLE_NAME)) {
			ILifecycleReference lifecycleRef = module.getDeclaredLifecycleDeclarations().get(module.getLifecycleName());
			lifecycleRef.getAllReferences().forEach(ref -> this.context.getCache().addEdge(ref.getModuleEdge()));
		}
		module.getDeclaredDependencies().forEach(dep -> this.context.getCache().addEdge(dep));

		this.modules.put(module.getIdentifier(), module);
	}

}
