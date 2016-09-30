
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
import com.unnsvc.rhena.builder.identifier.ComponentIdentifier;
import com.unnsvc.rhena.builder.identifier.Identifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;

public class RhenaModuleParser {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private RhenaContext context;
	private RhenaModule module;

	public RhenaModuleParser(RhenaContext context, ModuleIdentifier moduleIdentifier) {

		this.context = context;
		this.module = new RhenaModule(moduleIdentifier);
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
			if(extendsAttribute != null) {
					ModuleIdentifier extendsIdentifier = ModuleIdentifier.valueOf(extendsAttribute.getNodeValue());
					module.setParentModule(extendsIdentifier);
					context.addUnresolvedModuleIdentifier(extendsIdentifier);
			}
		}

		NodeList moduleChildren = moduleNode.getChildNodes();
		for (int i = 0; i < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if (moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_MODULE)) {
					processMetaNode(context, moduleChild);
				} else if (moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_DEPENDENCY)) {
					processDepenencyNode(moduleChild);
				}
			}
		}

		return module;
	}

	private void processMetaNode(RhenaContext context, Node moduleChild) throws DOMException, RhenaException {

		ComponentIdentifier componentIdentifier = new ComponentIdentifier(Identifier.valueOf(moduleChild.getAttributes().getNamedItem("component").getNodeValue()));
		Version version = Version.valueOf(moduleChild.getAttributes().getNamedItem("version").getNodeValue());
		ModuleIdentifier lifecycleDeclaration = null;
		if (moduleChild.getAttributes().getNamedItem("lifecycle") != null) {
			logger.debug("Lifecycle declaration: " + moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			lifecycleDeclaration = ModuleIdentifier.valueOf(moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			context.getUnresolvedIdentifiers().push(lifecycleDeclaration);
		}
		module.setComponent(componentIdentifier);
		if (version.equals(module.getModuleIdentifier().getVersion())) {
			module.setVersion(version);
		} else {
			throw new RhenaException("Requested version: " + version.toString() + " but got: " + module.getModuleIdentifier().getVersion().toString());
		}
		module.setLifecycleDeclaration(lifecycleDeclaration);
		
	}

	private void processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		String scopeString = moduleChild.getLocalName();
		Scope scope = Scope.valueOf(scopeString.toUpperCase());
		ModuleIdentifier module = ModuleIdentifier.valueOf(moduleChild.getAttributes().getNamedItem("module").getNodeValue());

		RhenaModuleEdge edge = new RhenaModuleEdge(scope, module);
	}
}
