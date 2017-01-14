
package com.unnsvc.rhena.lifecycle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.ICaller;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.lifecycle.EResourceType;
import com.unnsvc.rhena.common.lifecycle.IExecutionContext;
import com.unnsvc.rhena.common.lifecycle.IResource;
import com.unnsvc.rhena.common.lifecycle.Resource;
import com.unnsvc.rhena.common.model.IRhenaModule;

public class DefaultContext implements IExecutionContext {

	private static final long serialVersionUID = 1L;

	// @ProcessorContext
	// private IRhenaContext context;

	private List<IResource> resources;

	public DefaultContext() {

		resources = new ArrayList<IResource>();
	}

	/**
	 * @param type
	 * @TODO Better separation between sources and resources?
	 */
	@Override
	public void configure(ICaller caller, Document configuration) {

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
							if (srcPath.isDirectory()) {
								resources.add(new Resource(EResourceType.valueOf(resource.getLocalName().toUpperCase()), path, srcPath));
							}
						}
					}
				}
			}
		} else {
			// Defaults
			resources.add(new Resource(EResourceType.MAIN, "src/main/java", new File(moduleBasedir, "src/main/java")));
			resources.add(new Resource(EResourceType.MAIN, "src/main/resources", new File(moduleBasedir, "src/main/resources")));
			resources.add(new Resource(EResourceType.TEST, "src/test/java", new File(moduleBasedir, "src/test/java")));
			resources.add(new Resource(EResourceType.TEST, "src/test/resources", new File(moduleBasedir, "src/test/resources")));
		}
	}

	@Override
	public List<IResource> getResources() {

		return resources;
	}

	@Override
	public List<File> selectResources(EExecutionType type, String pattern) throws RhenaException {

		Pattern p = Pattern.compile(pattern);
		List<File> selected = new ArrayList<File>();
		EResourceType restype = null;

		if (type.equals(EExecutionType.MAIN)) {
			restype = EResourceType.MAIN;
		} else if (type.equals(EExecutionType.TEST)) {
			restype = EResourceType.TEST;
		}

		for (IResource res : resources) {
			if (res.getResourceType().equals(restype)) {
				try {
					selectRecursive(res.getAbsolutePath(), p, selected);
				} catch (IOException ioe) {
					throw new RhenaException(ioe.getMessage(), ioe);
				}
			}
		}

		return selected;
	}

	protected void selectRecursive(File srcPath, Pattern p, List<File> selected) throws IOException {

		if (srcPath.isFile()) {

			String pathStr = srcPath.getAbsoluteFile().getCanonicalPath();
			Matcher m = p.matcher(pathStr);
			if (m.find()) {
				selected.add(srcPath);
			}
		} else if (srcPath.isDirectory()) {

			for (File contained : srcPath.listFiles()) {
				selectRecursive(contained, p, selected);
			}
		}
	}

	@Override
	public File getOutputDirectory(IRhenaModule module) {

		File outputDirectory = new File(module.getLocation().getPath(), RhenaConstants.DEFAULT_OUTPUT_DIRECTORY_NAME);
		if (!outputDirectory.isDirectory()) {
			outputDirectory.mkdirs();
		}
		return outputDirectory;
	}
}
