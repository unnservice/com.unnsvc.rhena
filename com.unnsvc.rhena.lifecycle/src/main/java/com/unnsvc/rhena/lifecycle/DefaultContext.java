
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ExecutionType;
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

	private Logger log = LoggerFactory.getLogger(getClass());
	private Map<ExecutionType, List<IResource>> resources;
	// private Map<Class<? extends ILifecycleProcessor>, Object>

	public DefaultContext() {

		this.resources = new EnumMap<ExecutionType, List<IResource>>(ExecutionType.class);
	}

	/**
	 * @param type
	 * @TODO the idea is to part some sort of configuration and if values are
	 *       not set then use framework defaults
	 */
	@Override
	public void configure(IRhenaModule module, Document configuration) {

		File location = new File(module.getLocation().getPath()).getAbsoluteFile();
		
		this.resources.put(ExecutionType.DELIVERABLE, resourcesAsList(
				new Resource(new File(location, "src/main/java"), new File(location, "target/deliverable/classes")),
				new Resource(new File(location, "src/main/resources"), new File(location, "target/deliverable/classes"))
			));
		
		this.resources.put(ExecutionType.FRAMEWORK, resourcesAsList(
				new Resource(new File(location, "src/framework/java"), new File(location, "target/framework/classes")),
				new Resource(new File(location, "src/framework/resources"), new File(location, "target/framework/classes"))
			));
	}
	
	protected List<IResource> resourcesAsList(IResource... resources) {
		
		List<IResource> reslist = new ArrayList<IResource>();
		for(IResource res : resources) {
			reslist.add(res);
		}
		return reslist;
	}

	@Override
	public List<IResource> getResources(ExecutionType execution) {

		return resources.get(execution);
	}
}
