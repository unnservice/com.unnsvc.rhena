
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.lifecycle.resources.FilteredResource;
import com.unnsvc.rhena.lifecycle.resources.Resource;

public abstract class AbstractDefaultContext implements IExecutionContext {

	private static final long serialVersionUID = 1L;
	private List<IResource> resources;

	public AbstractDefaultContext() {

		resources = new ArrayList<IResource>();
	}

	/**
	 * @param type
	 * @throws IOException
	 * @TODO Better separation between sources and resources?
	 */
	@Override
	public void configure(ICaller caller, Document configuration) throws RhenaException {

		try {
			IRhenaModule module = caller.getModule();
			File moduleBasedir = new File(module.getLocation().getPath());

			if (configuration != null) {
				Node context = Utils.getChildNode(configuration, "context");
				if (context != null) {
					Node resourcesNode = Utils.getChildNode(context, "resources");
					if (resourcesNode != null) {
						NodeList resourcesChildren = resourcesNode.getChildNodes();
						for (int j = 0; j < resourcesChildren.getLength(); j++) {

							Node resource = resourcesChildren.item(j);
							if (resource.getNodeType() == Node.ELEMENT_NODE) {
								String path = resource.getAttributes().getNamedItem("path").getNodeValue();
								File srcPath = new File(moduleBasedir, path).getAbsoluteFile();
								boolean filter = false;
								if (resource.getAttributes().getNamedItem("filter") != null) {
									filter = Boolean.valueOf(resource.getAttributes().getNamedItem("filter").getNodeValue());
								}
								if (srcPath.isDirectory()) {
									EExecutionType restype = EExecutionType.valueOf(resource.getLocalName().toUpperCase());
									if (restype.equals(EExecutionType.MAIN)) {
										resources.add(newResource(restype, moduleBasedir, path, "target/main", filter));
									} else if (restype.equals(EExecutionType.TEST)) {
										resources.add(newResource(restype, moduleBasedir, path, "target/test", filter));
									}
								}
							}
						}
					}
				}
			} else {
				// Defaults
				resources.add(newResource(EExecutionType.MAIN, moduleBasedir, "src/main/java", "target/main", false));
				resources.add(newResource(EExecutionType.MAIN, moduleBasedir, "src/main/resources", "target/main", true));
				resources.add(newResource(EExecutionType.TEST, moduleBasedir, "src/test/java", "target/test", false));
				resources.add(newResource(EExecutionType.TEST, moduleBasedir, "src/test/resources", "target/test", true));
			}
		} catch (Throwable t) {
			throw new RhenaException(t.getMessage(), t);
		}
	}

	private IResource newResource(EExecutionType type, File moduleBaseDir, String relativeSourcePath, String relativeOutputPath, boolean filtering)
			throws IOException {

		if (filtering) {
			return new FilteredResource(type, moduleBaseDir.getCanonicalFile(), relativeSourcePath, relativeOutputPath, relativeOutputPath + "_filtered");
		} else {
			return new Resource(type, moduleBaseDir.getCanonicalFile(), relativeSourcePath, relativeOutputPath);
		}
	}

	@Override
	public List<IResource> getResources() {

		return resources;
	}

	@Override
	public void setResources(List<IResource> resources) {

		this.resources = resources;
	}
}
