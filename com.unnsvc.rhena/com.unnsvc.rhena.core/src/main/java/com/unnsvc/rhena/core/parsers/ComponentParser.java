package com.unnsvc.rhena.core.parsers;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.core.Constants;
import com.unnsvc.rhena.core.exceptions.RhenaException;
import com.unnsvc.rhena.core.exceptions.RhenaParserException;
import com.unnsvc.rhena.core.identifier.QualifiedIdentifier;
import com.unnsvc.rhena.core.identifier.Version;
import com.unnsvc.rhena.core.model.RhenaComponentEdge;
import com.unnsvc.rhena.core.model.RhenaDependencyEdge;
import com.unnsvc.rhena.core.model.RhenaComponent;
import com.unnsvc.rhena.core.model.RhenaProject;
import com.unnsvc.rhena.core.resolution.ResolutionEngine;

public class ComponentParser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResolutionEngine engine;
	private RhenaComponent componentDescriptor;

	public ComponentParser(ResolutionEngine engine, Document componentDescriptorDocument, String componentName) throws RhenaException {

		this.engine = engine;
		this.componentDescriptor = new RhenaComponent(componentName);
		parse(componentDescriptorDocument);
	}

	private void parse(Document componentDescriptorDocument) throws RhenaException {

		NodeList nl = componentDescriptorDocument.getChildNodes().item(0).getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {

				log.debug("Parsing node: " + node + " localName: " + node.getPrefix() + ":" + node.getLocalName() + " " + node.getNamespaceURI());

				String nodeName = node.getLocalName();

				if (node.getNamespaceURI().equals(Constants.NS_COMPONENT)) {

					if (nodeName.equals("import")) {

						handleImport(node);
					} else if (nodeName.equals("projects")) {

						NodeList projectNodes = node.getChildNodes();
						for (int projectNodeIndex = 0; projectNodeIndex < projectNodes.getLength(); projectNodeIndex++) {

							Node projectNode = projectNodes.item(projectNodeIndex);
							if (projectNode.getNodeType() == Node.ELEMENT_NODE) {

								handleProject(projectNodes.item(projectNodeIndex));
							}
						}
					} else if (nodeName.equals("version")) {

						handleVersion(node);
					}
				} else if (node.getNamespaceURI().equals(Constants.NS_DEPENDENCY)) {

					if (node.getPrefix().equals("dependency")) {

						RhenaDependencyEdge dependency = handleDependency(node);
						componentDescriptor.addDependency(dependency);
					}
				}
			}
		}
	}

	private RhenaDependencyEdge handleDependency(Node node) throws RhenaParserException {

		String scope = node.getLocalName();
		String id = node.getAttributes().getNamedItem("id").getNodeValue();
		// optional
		String resolverAttr = node.getAttributes().getNamedItem("resolver") == null ? null : node.getAttributes().getNamedItem("resolver").getNodeValue();

		ProjectIdentifier pi = QualifiedIdentifier.valueOfProject(id);

		String componentName = null;
		if (pi.getComponent() == null) {

			componentName = componentDescriptor.getComponentName();
		} else {

			componentName = pi.getComponent().toString();
		}

		String resolver = Constants.DEFAULT_RESOLVER;
		if (resolverAttr != null) {

			resolver = resolverAttr;
		}

		RhenaDependencyEdge dependency = new RhenaDependencyEdge(componentName, scope, pi.getProject().toString(), pi.getVersion().toString(), resolver);
		engine.addResolutionRequest(dependency);

		return dependency;
	}

	private void handleVersion(Node node) throws RhenaParserException, DOMException {

		Version version = Version.valueOf(node.getTextContent().trim());
		componentDescriptor.setVersion(version);
	}

	private void handleImport(Node node) {

		String componentId = node.getAttributes().getNamedItem("component").getNodeValue();
		RhenaComponentEdge componentImportEdge = new RhenaComponentEdge(componentId);
		engine.addResolutionRequest(componentImportEdge);
		componentDescriptor.addImport(componentImportEdge);
	}

	private void handleProject(Node node) throws RhenaParserException {

		String projectType = node.getLocalName();
		String projectName = node.getAttributes().getNamedItem("name").getNodeValue();
		RhenaProject project = new RhenaProject(componentDescriptor, projectType, projectName);

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNamespaceURI().equals(Constants.NS_DEPENDENCY)) {

					RhenaDependencyEdge dependency = handleDependency(child);
					project.addDependency(dependency);

				} else if (child.getNamespaceURI().equals(Constants.NS_COMPONENT)) {

					if (child.getLocalName().equals("properties")) {
						Properties props = handleProperties(child);
						project.setProperties(props);
					}
				}
			}
		}

		componentDescriptor.addProject(project);
	}

	private Properties handleProperties(Node child) {

		Properties props = new Properties();

		NodeList children = child.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node propNode = children.item(i);
			if (propNode.getNodeType() == Node.ELEMENT_NODE) {
				props.put(propNode.getLocalName(), propNode.getTextContent().trim());
			}
		}

		return props;
	}

	public RhenaComponent getComponentDescriptor() {

		return componentDescriptor;
	}
}
