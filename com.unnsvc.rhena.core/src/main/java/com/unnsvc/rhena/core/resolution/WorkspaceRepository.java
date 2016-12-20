
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IModelResolver;
import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;

public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IModelResolver resolver, IEntryPoint entryPoint) throws RhenaException {

		List<IArtifactDescriptor> deps = new ArrayList<IArtifactDescriptor>();
		IRhenaModule module = resolver.materialiseModel(entryPoint.getTarget());

		EExecutionType et = entryPoint.getExecutionType();
		// save deps of deps scopes
		for (EExecutionType dep : et.getTraversables()) {
			deps.addAll(getDepchain(cache, resolver, module, dep));
		}
		// requested scope deps
		deps.addAll(getDepchain(cache, resolver, module, et));

		// resolve lifecycle here and execute it
		// ILifecycleReference lifecycleRef =
		// module.getLifecycleDeclarations().get(module.getLifecycleName());

		// module.visit(new DepchainVisitor(resolver,
		// entryPoint.getExecutionType()));

		ModuleIdentifier identifier = entryPoint.getTarget();
		return new RhenaExecution(identifier, entryPoint.getExecutionType(), new ArtifactDescriptor(identifier.toString(), "http://not.implemented", "not-implemented"));
	}

	private List<IArtifactDescriptor> getDepchain(IRhenaCache cache, IModelResolver resolver, IRhenaModule module, EExecutionType et)
			throws RhenaException {

		List<IArtifactDescriptor> deps = new ArrayList<IArtifactDescriptor>();

		return deps;
	}
}
