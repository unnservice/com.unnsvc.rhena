
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

import com.unnsvc.rhena.Constants;
import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.builder.exceptions.RhenaException;
import com.unnsvc.rhena.builder.identifier.ComponentIdentifier;
import com.unnsvc.rhena.builder.identifier.Identifier;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.identifier.Version;

public class RhenaModuleParser {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private RhenaModule module;
	
	public RhenaModuleParser() {
		
		module = new RhenaModule();
	}

	public RhenaModule parse(RhenaContext context, String moduleName, URI uri) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(uri.toASCIIString());

		NodeList children = document.getChildNodes();
		Node moduleNode = children.item(0);
		
		Node extendsAttribute = moduleNode.getAttributes().getNamedItem("extends");
		
		if(extendsAttribute != null) {
			
			logger.debug("Parsing extends attribute: " + extendsAttribute.getNodeValue());
			ModuleIdentifier moduleIdentifier = ModuleIdentifier.valueOf(extendsAttribute.getNodeValue());
			context.addUnresolvedModuleIdentifier(moduleIdentifier);
			logger.debug("Parsed extends attribute to: " + moduleIdentifier.toString());
			module.setExtends(moduleIdentifier);
		}
		
		NodeList moduleChildren = moduleNode.getChildNodes();
		for(int i = 0;  i  < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if(moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if(moduleChild.getNamespaceURI().equals(Constants.NS_RHENA_MODULE)) {
					processMetaNode(context, moduleChild);
				}
			}
		}
		
		
		return module;
	}

	private void processMetaNode(RhenaContext context, Node moduleChild) throws DOMException, RhenaException {

		ComponentIdentifier componentIdentifier = new ComponentIdentifier(Identifier.valueOf(moduleChild.getAttributes().getNamedItem("component").getNodeValue()));
		Version version = Version.valueOf(moduleChild.getAttributes().getNamedItem("version").getNodeValue());
		ModuleIdentifier lifecycleDeclaration = null;
		if(moduleChild.getAttributes().getNamedItem("lifecycle") != null) {
			logger.debug("Lifecycle declaration: " + moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			lifecycleDeclaration = ModuleIdentifier.valueOf(moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			context.addUnresolvedModuleIdentifier(lifecycleDeclaration);
		}
		module.setComponent(componentIdentifier);
		module.setVersion(version);
		module.setLifecycleDeclaration(lifecycleDeclaration);
	}
}
