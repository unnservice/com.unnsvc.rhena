
package com.unnsvc.rhena.core;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.Constants;
import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.model.ModuleIdentifier;
import com.unnsvc.rhena.common.model.RhenaEdge;
import com.unnsvc.rhena.common.model.RhenaEdgeType;
import com.unnsvc.rhena.common.model.RhenaModel;
import com.unnsvc.rhena.common.model.RhenaReference;

public class RhenaModuleParser {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private RhenaModel module;

	public RhenaModuleParser(ModuleIdentifier moduleIdentifier, URI location, IRepository repository) throws RhenaException {

		this.module = new RhenaModel(moduleIdentifier, repository);
		try {
			parse(location);
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	public void parse(URI uri) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(uri.toASCIIString());

		NodeList children = document.getChildNodes();
		Node moduleNode = children.item(0);

		if (moduleNode.getAttributes().getNamedItem("extends") != null) {
			Node extendsAttribute = moduleNode.getAttributes().getNamedItem("extends");
			if (extendsAttribute != null) {
				String extendsModuleIdentifierStr = extendsAttribute.getNodeValue();
				ModuleIdentifier extendsModuleIdentifier = new ModuleIdentifier(extendsModuleIdentifierStr.split(":"));
				module.setParentModule(new RhenaReference(extendsModuleIdentifier));
			}
		}

		NodeList moduleChildren = moduleNode.getChildNodes();
		for (int i = 0; i < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if (moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_MODULE)) {
					processMetaNode(moduleChild);
				} else if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_DEPENDENCY)) {
					processDepenencyNode(moduleChild);
				}
			}
		}
	}

	private void processPropertyNode(Node propertyNode) {

		String nodeName = propertyNode.getLocalName();
		String nodeValue = propertyNode.getTextContent();
		logger.debug("Set property: " + nodeName + "=" + nodeValue + " on " + module.getModuleIdentifier());
		module.setProperty(nodeName, nodeValue);
	}

	private void processMetaNode(Node moduleChild) throws DOMException, RhenaException {

		String componentNameStr = moduleChild.getAttributes().getNamedItem("component").getNodeValue();
		String versionStr = moduleChild.getAttributes().getNamedItem("version").getNodeValue();

		if (moduleChild.getAttributes().getNamedItem("lifecycle") != null) {
			// logger.debug("Lifecycle declaration: " +
			// moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			ModuleIdentifier lifecycleDeclaration = new ModuleIdentifier(moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue().split(":"));
			module.setLifecycleModule(new RhenaReference(lifecycleDeclaration));
		}

		if (!module.getModuleIdentifier().getComponentName().toString().equals(componentNameStr)
				|| !module.getModuleIdentifier().getVersion().toString().equals(versionStr)) {
			throw new RhenaException("Not correct version in workspace for: " + module.getModuleIdentifier());
		}

		NodeList metaNodeChildren = moduleChild.getChildNodes();
		for (int i = 0; i < metaNodeChildren.getLength(); i++) {
			Node metaChild = metaNodeChildren.item(i);
			if (metaChild.getNodeType() == Node.ELEMENT_NODE) {
				if (metaChild.getLocalName().equals("properties")) {
					NodeList properties = metaChild.getChildNodes();
					for (int p = 0; p < properties.getLength(); p++) {
						Node propertyNode = properties.item(p);
						if (propertyNode.getNodeType() == Node.ELEMENT_NODE) {
							processPropertyNode(properties.item(p));
						}
					}
				} else if (metaChild.getLocalName().equals("lifecycles")) {
					NodeList lifecycleNodeList = metaChild.getChildNodes();
					for (int p = 0; p < lifecycleNodeList.getLength(); p++) {
						Node lifecycleNode = lifecycleNodeList.item(p);
						if (lifecycleNode.getNodeType() == Node.ELEMENT_NODE) {
							processLifecycleNode(lifecycleNode);
						}
					}
				}
			}
		}
	}

	private void processLifecycleNode(Node lifecycleNode) {

	}

	private void processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		String scopeString = moduleChild.getLocalName();
		RhenaEdgeType dependencyType = RhenaEdgeType.valueOf(scopeString.toUpperCase());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();

		ModuleIdentifier moduleIdentifier = new ModuleIdentifier(dependencyTargetModuleIdentifier.split(":"));
		RhenaEdge edge = new RhenaEdge(dependencyType, new RhenaReference(moduleIdentifier));
		module.addDependencyEdge(edge);
	}

	public RhenaModel getModel() {

		return module;
	}
}
