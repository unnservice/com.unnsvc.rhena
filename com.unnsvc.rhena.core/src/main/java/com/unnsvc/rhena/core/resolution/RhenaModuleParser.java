
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.common.model.executiontype.IExecutionType;
import com.unnsvc.rhena.core.lifecycle.ContextReference;
import com.unnsvc.rhena.core.lifecycle.GeneratorReference;
import com.unnsvc.rhena.core.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.core.lifecycle.ProcessorReference;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.model.RhenaModule;
import com.unnsvc.rhena.core.model.RhenaReference;

public class RhenaModuleParser {

	private Logger log = LoggerFactory.getLogger(getClass());
	private RhenaModule module;

	public RhenaModuleParser(ModuleIdentifier moduleIdentifier, URI projectLocationUri, IRepository repository) throws RhenaException {

		this.module = new RhenaModule(moduleIdentifier, projectLocationUri, repository);
		try {

			URI moduleDescriptorUri = new URI(projectLocationUri.toString() + "/module.xml").normalize();
			parse(moduleDescriptorUri);
		} catch (Exception ex) {
			throw new RhenaException(ex);
		}
	}

	public void parse(URI uri) throws Exception {

		Document document = parseAndValidateDocument(uri);

		NodeList children = document.getChildNodes();
		Node moduleNode = children.item(0);

		if (moduleNode.getAttributes().getNamedItem("extends") != null) {
			Node extendsAttribute = moduleNode.getAttributes().getNamedItem("extends");
			if (extendsAttribute != null) {
				String extendsModuleIdentifierStr = extendsAttribute.getNodeValue();
				ModuleIdentifier extendsModuleIdentifier = ModuleIdentifier.valueOf(extendsModuleIdentifierStr);
				module.setParentModule(new RhenaEdge(IExecutionType.MODEL, new RhenaReference(extendsModuleIdentifier), TraverseType.HIERARCHY));
			}
		}

		NodeList moduleChildren = moduleNode.getChildNodes();
		for (int i = 0; i < moduleChildren.getLength(); i++) {
			Node moduleChild = moduleChildren.item(i);
			if (moduleChild.getNodeType() == Node.ELEMENT_NODE) {
				if (moduleChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_MODULE)) {
					processMetaNode(moduleChild);
				} else if (moduleChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_DEPENDENCY)) {
					processDepenencyNode(moduleChild);
				}
			}
		}
	}

	private Document parseAndValidateDocument(URI uri) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(uri.toString());

		// validate
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/module.xsd"));

		try {
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (Exception ex) {
			log.error("Schema validation failed for : " + uri);
			throw new RhenaException("Schema validation error for: " + uri.toString(), ex);
		}

		return document;
	}

	private void processPropertyNode(Node propertyNode) {

		String nodeName = propertyNode.getLocalName();
		String nodeValue = propertyNode.getTextContent();
		module.setProperty(nodeName, nodeValue);
	}

	private void processMetaNode(Node moduleChild) throws DOMException, RhenaException {

		String componentNameStr = moduleChild.getAttributes().getNamedItem("component").getNodeValue();
		String versionStr = moduleChild.getAttributes().getNamedItem("version").getNodeValue();

		if (moduleChild.getAttributes().getNamedItem("lifecycle") != null) {
			// logger.debug("Lifecycle declaration: " +
			// moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue());
			String lifecycleName = moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue();
			module.setLifecycleName(lifecycleName);
		}

		if (!module.getModuleIdentifier().getComponentName().toString().equals(componentNameStr)
				|| !module.getModuleIdentifier().getVersion().toString().equals(versionStr)) {
			throw new RhenaException("Not correct version in workspace for: " + module.getModuleIdentifier());
		}

		NodeList metaNodeChildren = moduleChild.getChildNodes();
		for (int i = 0; i < metaNodeChildren.getLength(); i++) {
			Node metaChild = metaNodeChildren.item(i);
			if (metaChild.getNodeType() == Node.ELEMENT_NODE) {
				if (metaChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_PROPERTIES)) {
					processPropertyNode(metaChild);
				} else if (metaChild.getNamespaceURI().equals(RhenaConstants.NS_RHENA_MODULE)) {
					if (metaChild.getLocalName().equals("lifecycle")) {
						processLifecycleNode(metaChild);
					}
				}
			}
		}
	}

	private void processLifecycleNode(Node lifecycleNode) throws RhenaException {

		LifecycleDeclaration ld = new LifecycleDeclaration(lifecycleNode.getAttributes().getNamedItem("name").getNodeValue());

		NodeList children = lifecycleNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				String module = child.getAttributes().getNamedItem("module").getNodeValue();
				String clazz = child.getAttributes().getNamedItem("class").getNodeValue();
				String schema = null;
				if (child.getAttributes().getNamedItem("schema") != null) {
					schema = child.getAttributes().getNamedItem("schema").getNodeValue();
				}

				// Make a document out of the entire processor node
				Document config = nodeToDocument(child);

				IExecutionType et = IExecutionType.FRAMEWORK;
				TraverseType tt = TraverseType.SCOPE;

				if (child.getLocalName().equals("context")) {

					ContextReference configurator = new ContextReference(new RhenaReference(ModuleIdentifier.valueOf(module)), clazz, schema, config, et, tt);
					ld.setContext(configurator);
				} else if (child.getLocalName().equals("processor")) {

					ProcessorReference processor = new ProcessorReference(new RhenaReference(ModuleIdentifier.valueOf(module)), clazz, schema, config, et, tt);
					ld.addProcessor(processor);
				} else if (child.getLocalName().equals("generator")) {

					GeneratorReference generator = new GeneratorReference(new RhenaReference(ModuleIdentifier.valueOf(module)), clazz, schema, config, et, tt);
					ld.setGenerator(generator);
				}
			}
		}

		module.getLifecycleDeclarations().put(ld.getName(), ld);
	}

	private Document nodeToDocument(Node child) throws RhenaException {

		Document document = Utils.newEmptyDocument();
		Node importedNode = document.importNode(child, true);
		document.appendChild(importedNode);
		return document;
	}

	private void processDepenencyNode(Node moduleChild) throws DOMException, RhenaException {

		IExecutionType dependencyType = Utils.valueOf(moduleChild.getLocalName());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();

		ModuleIdentifier moduleIdentifier = ModuleIdentifier.valueOf(dependencyTargetModuleIdentifier);
		RhenaEdge edge = null;

		if (moduleChild.getAttributes().getNamedItem("traverse") != null) {

			TraverseType traverseType = TraverseType.valueOf(moduleChild.getAttributes().getNamedItem("traverse").getNodeValue().toUpperCase());
			edge = new RhenaEdge(dependencyType, new RhenaReference(moduleIdentifier), traverseType);
		} else {

			/**
			 * Default to scope traversal for deliverables
			 */
			TraverseType traverseType = TraverseType.NONE;
			if (dependencyType.equals(IExecutionType.DELIVERABLE)) {
				traverseType = TraverseType.SCOPE;
			}
			edge = new RhenaEdge(dependencyType, new RhenaReference(moduleIdentifier), traverseType);
		}

		if (!module.getDependencyEdges().contains(edge)) {
			module.getDependencyEdges().add(edge);
		}
	}

	public RhenaModule getModel() {

		return module;
	}
}
