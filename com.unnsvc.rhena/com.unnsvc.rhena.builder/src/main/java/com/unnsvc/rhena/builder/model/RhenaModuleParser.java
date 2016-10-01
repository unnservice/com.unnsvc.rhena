
package com.unnsvc.rhena.builder.model;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.builder.Constants;
import com.unnsvc.rhena.builder.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;

public class RhenaModuleParser {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private RhenaModule module;
	private RhenaContext context;

	public RhenaModuleParser(RhenaContext context) {

		this.context = context;
		this.module = new RhenaModule();
	}

	public RhenaModule parse(String moduleName, URI uri) throws Exception {

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
				ModuleIdentifier extendsModuleIdentifier = context.newModuleIdentifier(extendsModuleIdentifierStr);
				module.setParentModule(new RhenaModuleEdge(Scope.MODEL, extendsModuleIdentifier));
			}
		}

		NodeList moduleChildren = moduleNode.getChildNodes();
		for (int i = 0; i < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if (moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_MODULE)) {
					processMetaNode(moduleChild, moduleName);
				} else if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_DEPENDENCY)) {
					processDepenencyNode(moduleChild);
				}
			}
		}

		return module;
	}

	private void processMetaNode(Node moduleChild, String moduleNameStr) throws DOMException, RhenaException {

		String componentNameStr = moduleChild.getAttributes().getNamedItem("component").getNodeValue();
		String versionStr = moduleChild.getAttributes().getNamedItem("version").getNodeValue();

		if (moduleChild.getAttributes().getNamedItem("lifecycle") != null) {
			logger.debug("Lifecycle declaration: " + moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			ModuleIdentifier lifecycleDeclaration = context.newModuleIdentifier(moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			module.setLifecycleModule(new RhenaModuleEdge(Scope.LIFECYCLE, lifecycleDeclaration));
		}

		ModuleIdentifier moduleIdentifier = context.newModuleIdentifier(componentNameStr, moduleNameStr, versionStr);
		module.setModuleIdentifier(moduleIdentifier);
	}

	private void processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		String scopeString = moduleChild.getLocalName();
		Scope scope = Scope.valueOf(scopeString.toUpperCase());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();
		ModuleIdentifier moduleIdentifier = context.newModuleIdentifier(dependencyTargetModuleIdentifier);

		RhenaModuleEdge edge = new RhenaModuleEdge(scope, moduleIdentifier);
		module.addDependencyEdge(edge);
	}
}
