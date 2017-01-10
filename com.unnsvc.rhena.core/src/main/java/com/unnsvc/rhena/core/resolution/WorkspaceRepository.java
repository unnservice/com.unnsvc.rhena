
package com.unnsvc.rhena.core.resolution;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.execution.IRhenaExecution;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IEntryPoint;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IGenerator;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycle;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessor;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleProcessorReference;
import com.unnsvc.rhena.common.model.lifecycle.ILifecycleReference;
import com.unnsvc.rhena.common.model.lifecycle.IProcessor;
import com.unnsvc.rhena.common.model.lifecycle.IProcessorReference;
import com.unnsvc.rhena.core.execution.ArtifactDescriptor;
import com.unnsvc.rhena.core.execution.WorkspaceExecution;
import com.unnsvc.rhena.core.lifecycle.Lifecycle;
import com.unnsvc.rhena.core.visitors.Dependencies;
import com.unnsvc.rhena.core.visitors.DependencyCollectionVisitor;
import com.unnsvc.rhena.lifecycle.DefaultContext;
import com.unnsvc.rhena.lifecycle.DefaultGenerator;
import com.unnsvc.rhena.lifecycle.DefaultJavaProcessor;
import com.unnsvc.rhena.lifecycle.DefaultManifestProcessor;

/**
 * @TODO cache lifecycle over multiple executions?
 * @TODO MODEL gets generated here in the workspace repository?
 * @author noname
 *
 */
public class WorkspaceRepository extends AbstractWorkspaceRepository {

	public WorkspaceRepository(IRhenaContext context, File location) {

		super(context, location);
	}

	@Override
	public IRhenaExecution materialiseExecution(IRhenaCache cache, IEntryPoint entryPoint) throws RhenaException {

		IRhenaModule module = cache.getModule(entryPoint.getTarget());

		if (entryPoint.getExecutionType().equals(EExecutionType.MODEL)) {

			File workspaceDirectory = new File(module.getLocation().getPath());
			File moduleDescriptor = new File(workspaceDirectory, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			try {
				return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(), new ArtifactDescriptor(entryPoint.getTarget().toString(),
						moduleDescriptor.getCanonicalFile().toURI().toURL(), Utils.generateSha1(moduleDescriptor)));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		} else {

			Dependencies deps = new Dependencies(entryPoint.getExecutionType());

			// get dependency chains of dependencies
			getDepchain(deps, cache, entryPoint.getTarget(), entryPoint.getExecutionType());

			/**
			 * Up to, but not with, the ordinal, becauuse that's the one we will
			 * create next by executing a lifecycle
			 */
			for (int i = 0; i < entryPoint.getExecutionType().ordinal(); i++) {

				IRhenaExecution exec = cache.getExecution(entryPoint.getTarget()).get(EExecutionType.values()[i]);
				deps.addDependency(EExecutionType.values()[i], exec);
			}

			LifecycleBuilder lifecycleBuilder = new LifecycleBuilder(module, context);
			ILifecycle lifecycle = lifecycleBuilder.buildLifecycle(entryPoint, module.getLifecycleName());
			
			File generated = lifecycle.executeLifecycle(module, entryPoint.getExecutionType(), deps);
			
			try {
				return new WorkspaceExecution(entryPoint.getTarget(), entryPoint.getExecutionType(),
						new ArtifactDescriptor(entryPoint.getTarget().toString(), generated.getCanonicalFile().toURI().toURL(), Utils.generateSha1(generated)));
			} catch (IOException mue) {
				throw new RhenaException(mue.getMessage(), mue);
			}
		}
	}



	private void getDepchain(Dependencies deps, IRhenaCache cache, ModuleIdentifier identifier, EExecutionType et) throws RhenaException {

		IRhenaModule module = cache.getModule(identifier);

		/**
		 * Collect dependency information
		 */
		for (IRhenaEdge edge : module.getDependencies()) {
			IRhenaModule depmod = cache.getModule(identifier);
			DependencyCollectionVisitor coll = new DependencyCollectionVisitor(cache, edge);
			depmod.visit(coll);

			for (IRhenaExecution exec : coll.getDependencies()) {

				deps.addDependency(edge.getEntryPoint().getExecutionType(), exec);
			}
		}
	}
}
