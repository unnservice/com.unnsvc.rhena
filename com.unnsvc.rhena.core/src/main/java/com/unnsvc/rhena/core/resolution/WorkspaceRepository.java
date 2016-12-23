
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaConfiguration;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IArtifactDescriptor;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.RhenaExecution;
import com.unnsvc.rhena.core.visitors.DependencyCollector;

public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaConfiguration config, File location) {

		super(config, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		// config.getLogger(getClass()).debug(entryPoint.getTarget(), "creating
		// execution for " + entryPoint);

		List<IArtifactDescriptor> deps = new ArrayList<IArtifactDescriptor>();
		IRhenaModule module = cache.getModule(entryPoint.getTarget());

		// List<IRhenaExecution> exec = new ArrayList<IRhenaExecution>();
		//
		// DependencyCollectorOld coll = new DependencyCollectorOld(cache,
		// entryPoint.getExecutionType(), ESelectionType.SCOPE);
		// module.visit(coll);
		// if(!coll.getDependencies().isEmpty()) {
		// coll.getDependencies().forEach(dep ->
		// config.getLogger(getClass()).debug(module.getIdentifier(),
		// "Collected: " + dep + " upon building " +
		// entryPoint.getExecutionType())
		// );
		// }

		for (IRhenaEdge edge : module.getDependencies()) {
			IRhenaModule depmod = cache.getModule(edge.getEntryPoint().getTarget());
			DependencyCollector coll = new DependencyCollector(cache, edge);
			depmod.visit(coll);
			if (!coll.getDependencies().isEmpty()) {
				coll.getDependencies().forEach(dep -> config.getLogger(getClass()).debug(module.getIdentifier(), "Collected: " + dep + " upon building " + entryPoint.getExecutionType()));
			}
		}

		EExecutionType et = entryPoint.getExecutionType();
		// save deps of deps scopes
		for (EExecutionType dep : et.getTraversables()) {
			deps.addAll(getDepchain(cache, module, dep));
		}
		// requested scope deps
		deps.addAll(getDepchain(cache, module, et));

		// resolve lifecycle here and execute it
		// ILifecycleReference lifecycleRef =
		// module.getLifecycleDeclarations().get(module.getLifecycleName());

		// module.visit(new DepchainVisitor(resolver,
		// entryPoint.getExecutionType()));

		ModuleIdentifier identifier = entryPoint.getTarget();
		return new RhenaExecution(identifier, entryPoint.getExecutionType(),
				new ArtifactDescriptor(identifier.toString(), "http://not.implemented", "not-implemented"));
	}

	private List<IArtifactDescriptor> getDepchain(IRhenaCache cache, IRhenaModule module, EExecutionType et) throws RhenaException {

		List<IArtifactDescriptor> deps = new ArrayList<IArtifactDescriptor>();

		return deps;
	}
}
