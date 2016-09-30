
package com.unnsvc.rhena.builder.parsers;

import java.net.URI;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.RhenaContext;
import com.unnsvc.rhena.builder.identifier.ModuleIdentifier;
import com.unnsvc.rhena.builder.model.RhenaModule;
import com.unnsvc.rhena.builder.model.RhenaModuleEdge;

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
			module.setExtends(new RhenaModuleEdge(moduleIdentifier));
		}
		
		
		return module;
	}

}
