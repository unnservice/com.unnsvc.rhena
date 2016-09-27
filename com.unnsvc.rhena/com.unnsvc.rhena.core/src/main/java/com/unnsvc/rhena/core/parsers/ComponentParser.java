package com.unnsvc.rhena.core.parsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.Version;
import com.unnsvc.rhena.core.model.ComponentImportEdge;
import com.unnsvc.rhena.core.model.RhenaComponentDescriptor;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.ResolutionContext;

public class ComponentParser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResolutionContext context;
	private RhenaComponentDescriptor componentDescriptor;

	public ComponentParser(ResolutionContext context, Document componentDescriptorDocument, String componentName)
			throws RhenaException {

		this.context = context;
		this.componentDescriptor = new RhenaComponentDescriptor(componentName);
		parse(componentDescriptorDocument);
	}

	private void parse(Document componentDescriptorDocument) throws RhenaException {

		NodeList nl = componentDescriptorDocument.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				if (node.getLocalName().equals("import")) {

					handleImport(node);
				} else if (node.getLocalName().equals("project")) {

					handleProject(node);
				} else if (node.getLocalName().equals("version")) {
					
					handleVersion(node);
				}
			}
		}
	}

	private void handleVersion(Node node) throws RhenaParserException, DOMException {
		
		Version version = Version.valueOf(node.getTextContent());
		componentDescriptor.setVersion(version);
	}

	private void handleImport(Node node) {

		String componentId = node.getAttributes().getNamedItem("component").getNodeValue();
		ComponentImportEdge componentImportEdge = new ComponentImportEdge(componentId);
		context.getEngine().addResolutionRequest(componentImportEdge);
		componentDescriptor.addImport(componentImportEdge);
	}

	private void handleProject(Node node) {

		String projectName = node.getAttributes().getNamedItem("name").getNodeValue();
		RhenaProject project = new RhenaProject(componentDescriptor, projectName);
		componentDescriptor.addProject(project);
	}

	public RhenaComponentDescriptor getComponentDescriptor() {

		return componentDescriptor;
	}
}
