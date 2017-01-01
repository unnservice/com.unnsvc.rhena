
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

import com.unnsvc.rhena.common.IRhenaCache;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.model.IRhenaModule;
import com.unnsvc.rhena.common.model.lifecycle.IExecutionContext;
import com.unnsvc.rhena.lifecycle.paths.EResourceType;
import com.unnsvc.rhena.lifecycle.paths.Resource;

public class DefaultContext implements IExecutionContext {

	// @TODO injection mechanism
	// @ProcessorContext
	// private IRhenaModule module;
	// @ProcessorContext
	// private ExecutionType type;

	private List<Resource> resources;

	public DefaultContext(IRhenaCache cache) {

		resources = new ArrayList<Resource>();
	}

	/**
	 * @param type
	 * @TODO Better separation between sources and resources?
	 */
	@Override
	public void configure(IRhenaModule module, Document configuration) {

		File moduleBasedir = new File(module.getLocation().getPath());

		Node context = Utils.getChildNode(configuration, "context");
		if (context != null) {
			Node resourcesNode = Utils.getChildNode(context, "resources");
			if (resourcesNode != null) {
				NodeList resourcesChildren = resourcesNode.getChildNodes();
				for (int j = 0; j < resourcesChildren.getLength(); j++) {

					Node resource = resourcesChildren.item(j);
					if (resource.getNodeType() == Node.ELEMENT_NODE) {
						String path = resource.getAttributes().getNamedItem("path").getNodeValue();
						File srcPath = new File(moduleBasedir, path);
						if (srcPath.isDirectory()) {
							resources.add(new Resource(EResourceType.valueOf(resource.getLocalName().toUpperCase()), srcPath));
						}
					}
				}
			}
		}
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

		for (Resource res : resources) {
			if (res.getType().equals(restype)) {
				try {
					selectRecursive(res.getSrcPath(), p, selected);
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

}
