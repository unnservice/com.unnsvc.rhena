
package com.unnsvc.rhena.lifecycle;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.unnsvc.rhena.common.model.ExecutionType;
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
	public void configure(Document configuration) {

		List<IResource> deliverable = new ArrayList<IResource>();
		deliverable.add(new Resource("src/main/java", "target/classes"));
		deliverable.add(new Resource("src/main/resoources", "target/classes"));
		this.resources.put(ExecutionType.DELIVERABLE, resourcesAsList());
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
