
package com.unnsvc.rhena.core.model;

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
import com.unnsvc.rhena.common.IRhenaContext;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;
import com.unnsvc.rhena.common.model.ESelectionType;
import com.unnsvc.rhena.common.model.IRhenaEdge;
import com.unnsvc.rhena.core.lifecycle.ContextReference;
import com.unnsvc.rhena.core.lifecycle.GeneratorReference;
import com.unnsvc.rhena.core.lifecycle.LifecycleReference;
import com.unnsvc.rhena.core.lifecycle.ProcessorReference;

public class RhenaModuleParser {

	private RhenaModule module;
	private IRhenaContext context;

	public RhenaModuleParser(IRhenaContext context, IRepository repository, ModuleIdentifier identifier, URI descriptorLocation) throws RhenaException {

		try {
			this.context = context;
			URI location = new URI(descriptorLocation.getPath().substring(0, descriptorLocation.getPath().lastIndexOf("/")));
			this.module = new RhenaModule(identifier, location, repository);

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
				module.setParent(newEdge(module.getIdentifier(), EExecutionType.MODEL, extendsModuleIdentifier, ESelectionType.HIERARCHY));
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

		if (!module.getIdentifier().getComponentName().toString().equals(componentNameStr)) {
			throw new RhenaException("Component mismatch between: " + module.getIdentifier().getComponentName() + " and declared: " + componentNameStr);
		} else if (!module.getIdentifier().getVersion().toString().equals(versionStr)) {
			throw new RhenaException("Version mismatch between: " + module.getIdentifier().getVersion() + " and declared: " + versionStr);
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

		LifecycleReference lifecycleReference = new LifecycleReference(lifecycleNode.getAttributes().getNamedItem("name").getNodeValue());

		NodeList children = lifecycleNode.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				String moduleAttrStr = child.getAttributes().getNamedItem("module").getNodeValue();
				String clazzAttrStr = child.getAttributes().getNamedItem("class").getNodeValue();
				String schemaAttrStr = null;
				if (child.getAttributes().getNamedItem("schema") != null) {
					schemaAttrStr = child.getAttributes().getNamedItem("schema").getNodeValue();
				}

				// Make a document out of the entire processor node
				Document config = nodeToDocument(child);

				EExecutionType et = EExecutionType.MAIN;
				ESelectionType tt = ESelectionType.SCOPE;

				IRhenaEdge edge = newEdge(module.getIdentifier(), et, ModuleIdentifier.valueOf(moduleAttrStr), tt);
				if (child.getLocalName().equals("context")) {

					ContextReference configurator = new ContextReference(edge, clazzAttrStr, schemaAttrStr, config);
					lifecycleReference.setContext(configurator);
				} else if (child.getLocalName().equals("processor")) {

					ProcessorReference processor = new ProcessorReference(edge, clazzAttrStr, schemaAttrStr, config);
					lifecycleReference.addProcessor(processor);
				} else if (child.getLocalName().equals("generator")) {

					GeneratorReference generator = new GeneratorReference(edge, clazzAttrStr, schemaAttrStr, config);
					lifecycleReference.setGenerator(generator);
				}
			}
		}

		module.getLifecycleDeclarations().put(lifecycleReference.getName(), lifecycleReference);
	}

	private IRhenaEdge newEdge(ModuleIdentifier source, EExecutionType type, ModuleIdentifier target, ESelectionType selection) {

		IRhenaEdge edge = new RhenaEdge(source, new EntryPoint(type, target), selection);
		context.getCache().addEdge(edge);
		return edge;
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

			ESelectionType traverseType = ESelectionType.valueOf(moduleChild.getAttributes().getNamedItem("traverse").getNodeValue().toUpperCase());
			edge = newEdge(module.getIdentifier(), dependencyType, moduleIdentifier, traverseType);
		} else {

			/**
			 * Default to scope traversal for deliverables
			 */
			ESelectionType traverseType = ESelectionType.NONE;
			if (dependencyType.equals(EExecutionType.MAIN)) {
				traverseType = ESelectionType.SCOPE;
			}
			edge = newEdge(module.getIdentifier(), dependencyType, moduleIdentifier, traverseType);
		}

		if (!module.getDependencies().contains(edge)) {
			module.getDependencies().add(edge);
		}
	}

	public RhenaModule getModel() {

		return module;
	}
}
