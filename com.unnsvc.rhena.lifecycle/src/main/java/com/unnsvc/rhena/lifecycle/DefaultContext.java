
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import com.unnsvc.rhena.common.IResolutionContext;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.model.lifecycle.IResource;
import com.unnsvc.rhena.lifecycle.resource.Resource;

public class DefaultContext implements IExecutionContext {

	// // inject these later so we don't get constructor injection complexities
	// later
	// @ExecutionContext
	// private IRhenaModule module;
	// @ExecutionContext
	// private ExecutionType type;
	// @ExecutionContext // this will require more complex checks on lifecycle
	// phase to know whether the configurator is ready and has executed...
	// private Document configuration

	// private Logger log = LoggerFactory.getLogger(getClass());
	private Map<EExecutionType, List<IResource>> resources;
	// private Map<Class<? extends ILifecycleProcessor>, Object>

	public DefaultContext(IResolutionContext context) {

		this.resources = new HashMap<EExecutionType, List<IResource>>();
	}

	/**
	 * @param type
	 * @TODO the idea is to part some sort of configuration and if values are
	 *       not set then use framework defaults
	 */
	@Override
	public void configure(IRhenaModule module, Document configuration) {

		File location = new File(module.getLocation().getPath()).getAbsoluteFile();

		for (EExecutionType type : EExecutionType.values()) {

			if (type != EExecutionType.MODEL) {
				String actual = type.literal();
				if (type == EExecutionType.DELIVERABLE) {
					actual = "main";
				}

				this.resources.put(type,
						resourcesAsList(new Resource(new File(location, "src/" + actual + "/java"), new File(location, "target/" + actual + "/classes")),
								new Resource(new File(location, "src/" + actual + "/resources"), new File(location, "target/" + actual + "/classes"))));
			}
		}
	}

	protected List<IResource> resourcesAsList(IResource... resources) {

		List<IResource> reslist = new ArrayList<IResource>();
		for (IResource res : resources) {
			reslist.add(res);
		}
		return reslist;
	}

	@Override
	public List<IResource> getResources(EExecutionType execution) {

		return resources.get(execution);
	}
}
