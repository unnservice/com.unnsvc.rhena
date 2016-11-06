
package com.unnsvc.rhena.core.resolution;

import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.rhena.common.IRepository;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.common.model.TraverseType;
import com.unnsvc.rhena.core.lifecycle.ContextReference;
import com.unnsvc.rhena.core.lifecycle.GeneratorReference;
import com.unnsvc.rhena.core.lifecycle.LifecycleDeclaration;
import com.unnsvc.rhena.core.lifecycle.ProcessorReference;
import com.unnsvc.rhena.core.model.RhenaEdge;
import com.unnsvc.rhena.core.model.RhenaModule;

public class RhenaModuleParser {

	private RhenaModule module;

	public RhenaModuleParser(IRepository repository, ModuleIdentifier identifier, URI descriptorLocation) throws RhenaException {

		this.module = new RhenaModule(identifier, repository);
		try {
			parse(descriptorLocation);
		} catch (Exception re) {
			throw new RhenaException(re.getMessage(), re);
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
				module.setParent(new RhenaEdge(EExecutionType.MODEL, extendsModuleIdentifier, TraverseType.HIERARCHY));
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

			String lifecycleName = moduleChild.getAttributes().getNamedItem("lifecycle").getNodeValue();
			module.setLifecycleName(lifecycleName);
		}

		if (!module.getIdentifier().getComponentName().toString().equals(componentNameStr)
				|| !module.getIdentifier().getVersion().toString().equals(versionStr)) {
			throw new RhenaException("Not correct version in workspace for: " + module.getIdentifier());
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

				EExecutionType et = EExecutionType.FRAMEWORK;
				TraverseType tt = TraverseType.SCOPE;

				if (child.getLocalName().equals("context")) {

					IRhenaEdge edge = new RhenaEdge(et, ModuleIdentifier.valueOf(module), tt);
					ContextReference configurator = new ContextReference(edge, clazz, schema, config);
					ld.setContext(configurator);
				} else if (child.getLocalName().equals("processor")) {

					IRhenaEdge edge = new RhenaEdge(et, ModuleIdentifier.valueOf(module), tt);
					ProcessorReference processor = new ProcessorReference(edge, clazz, schema, config);
					ld.addProcessor(processor);
				} else if (child.getLocalName().equals("generator")) {

					IRhenaEdge edge = new RhenaEdge(et, ModuleIdentifier.valueOf(module), tt);
					GeneratorReference generator = new GeneratorReference(edge, clazz, schema, config);
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

		EExecutionType dependencyType = Utils.valueOf(moduleChild.getLocalName());
		String dependencyTargetModuleIdentifier = moduleChild.getAttributes().getNamedItem("module").getNodeValue();

		ModuleIdentifier moduleIdentifier = ModuleIdentifier.valueOf(dependencyTargetModuleIdentifier);
		IRhenaEdge edge = null;

		if (moduleChild.getAttributes().getNamedItem("traverse") != null) {

			TraverseType traverseType = TraverseType.valueOf(moduleChild.getAttributes().getNamedItem("traverse").getNodeValue().toUpperCase());
			edge = new RhenaEdge(dependencyType, moduleIdentifier, traverseType);
		} else {

			/**
			 * Default to scope traversal for deliverables
			 */
			TraverseType traverseType = TraverseType.NONE;
			if (dependencyType.equals(EExecutionType.DELIVERABLE)) {
				traverseType = TraverseType.SCOPE;
			}
			edge = new RhenaEdge(dependencyType, moduleIdentifier, traverseType);
		}

		if (!module.getDependencies().contains(edge)) {
			module.getDependencies().add(edge);
		}
	}

	public RhenaModule getModel() {

		return module;
	}
}
